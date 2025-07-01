package com.example.findyourself.model

data class ActiveChat(
    val userId: String = "",
    val participantId: String = "",
    val chatId: String = "",
    val createdAt: Long = 0L,
    val isEnded : Boolean = false
)