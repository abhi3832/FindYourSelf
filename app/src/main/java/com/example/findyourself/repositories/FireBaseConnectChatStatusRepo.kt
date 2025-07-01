package com.example.findyourself.repositories

import android.util.Log
import com.example.findyourself.dataClasses.Chat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


data class ActiveChat(
    val userId: String = "",
    val participantId: String = "",
    val chatId: String = "",
    val createdAt: Long = 0L,
    val isEnded : Boolean = false
)


@Singleton
class FirebaseConnectChatStatusRepo @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun observeChatAsFlow(chatId: String): Flow<Chat?> = callbackFlow {
        val listener = firestore.collection("currentlyChatting")
            .document(chatId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("FirestoreObserve", "Error observing chat", error)
                    close(error) // Optional: close flow with error
                    return@addSnapshotListener
                }

                val chat = snapshot?.toObject(Chat::class.java)
                trySend(chat).onFailure {
                    Log.e("FirestoreObserve", "Failed to emit chat: ${it?.message}")
                }
            }

        // Remove the listener when collection is cancelled
        awaitClose { listener.remove() }
    }


    suspend fun markChatAsEnded(chatId: String): Boolean {
        return try {
            firestore.collection("currentlyChatting")
                .document(chatId)
                .update("isEnded", true)
                .await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun removeUserFromParticipants(chatId: String, userId: String) {
        val firestore = FirebaseFirestore.getInstance()
        val chatRef = firestore.collection("currentlyChatting").document(chatId)
        val messagesRef = chatRef.collection("messages")

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(chatRef)
            val currentParticipants = snapshot.get("participants") as? List<*> ?: emptyList<Any>()
            val updatedParticipants = currentParticipants.filterNot { it == userId }

            if (updatedParticipants.isEmpty()) {
                // Delete chat doc
                transaction.delete(chatRef)
                return@runTransaction true // Mark that we should delete messages after transaction
            } else {
                transaction.update(chatRef, "participants", updatedParticipants)
                return@runTransaction false // No need to delete messages
            }
        }.addOnSuccessListener { shouldDeleteMessages ->
            Log.d("Chat", "User removed. Should delete messages: $shouldDeleteMessages")

            if (shouldDeleteMessages) {
                // Delete messages only if chat was deleted
                messagesRef.get().addOnSuccessListener { snapshot ->
                    val batch = firestore.batch()
                    snapshot.documents.forEach { doc ->
                        batch.delete(doc.reference)
                    }
                    batch.commit().addOnSuccessListener {
                        Log.d("Chat", "All messages deleted.")
                    }.addOnFailureListener {
                        Log.e("Chat", "Failed to delete messages: ${it.message}")
                    }
                }
            }

        }.addOnFailureListener {
            Log.e("Chat", "Error removing user: ${it.message}")
        }
    }


}
