package com.example.findyourself.repositories

import com.example.findyourself.dataClasses.Message
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

class FirebaseMessageRepository {

    private val firestore = Firebase.firestore

    fun sendMessage(chatId: String, message: Message): Flow<Result<Unit>> = flow {
        try {
            val messagesRef = firestore.collection("currentlyChatting")
                .document(chatId)
                .collection("messages")

            messagesRef.add(message).await()
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun observeMessages(chatId: String): Flow<List<Message>> = callbackFlow {
        val messagesRef = firestore.collection("currentlyChatting")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)

        val listener = messagesRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                val messages = snapshot.documents.mapNotNull { it.toObject(Message::class.java) }
                trySend(messages)
            }
        }

        awaitClose { listener.remove() }
    }
}
