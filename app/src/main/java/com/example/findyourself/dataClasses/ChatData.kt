package com.example.findyourself.dataClasses

import com.google.firebase.firestore.PropertyName
import kotlinx.serialization.Serializable


data class Chat(
    val chatId: String = "",
    val participants: List<String> = emptyList(),
    var lastMessage: String? = null,
    var lastMessageTimestamp: Long = 0L,
    val createdAt: Long = 0L,
    val typing: List<String> = emptyList(),
    @get:PropertyName("isEnded")
    @set:PropertyName("isEnded")
    var isEnded: Boolean = false
)

data class TypingStatus(
    val userId: String = "",
    val isTyping: Boolean = false
)



data class Message(
    val messageId: String = "",
    val senderId: String = "",
    val text: String? = null,
    val timestamp: Long = 0L,
    val type: String = "text",
    val mediaUrl: String? = null,
    val isRead: Boolean = false,
    val replyToMessageId: String? = null,
)

