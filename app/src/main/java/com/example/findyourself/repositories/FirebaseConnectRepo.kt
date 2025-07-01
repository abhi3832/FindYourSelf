package com.example.findyourself.repositories

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.findyourself.dataClasses.Chat
import com.example.findyourself.dataClasses.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseConnectChatRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun createChatDocumentIfNeeded(chatId: String, user1: User, user2: User): Result<Unit> {
        return try {
            val chatRef = firestore.collection("currentlyChatting").document(chatId)
            val snapshot = chatRef.get().await()
            if (!snapshot.exists()) {
                Log.d("ConnectJaipur", "Chat Document Doesn't Exist")
                val currentTimestamp = System.currentTimeMillis()
                val chat = Chat(
                    chatId = chatId,
                    participants = listOf(user1.uid, user2.uid),
                    typing = listOf(user1.uid, user2.uid),
                    lastMessage = null,
                    lastMessageTimestamp = 0L,
                    createdAt = currentTimestamp,
                    isEnded = false
                )
                chatRef.set(chat).await()
                Log.d("ConnectJaipur", "Chat Document Created Successfully!")
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Log.d("ConnectJaipur", "Exception Creating Chat Document: ${e.message}")
            Result.failure(e)
        }
    }

}
