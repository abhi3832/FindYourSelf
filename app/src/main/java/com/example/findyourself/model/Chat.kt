package com.example.findyourself.model

import com.google.firebase.firestore.PropertyName


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