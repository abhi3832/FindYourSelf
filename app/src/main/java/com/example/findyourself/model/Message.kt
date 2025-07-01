package com.example.findyourself.model

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