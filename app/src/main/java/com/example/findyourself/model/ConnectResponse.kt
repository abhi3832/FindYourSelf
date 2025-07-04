package com.example.findyourself.model

import kotlinx.serialization.Serializable

@Serializable
data class ConnectResponse(
    val isConnected : Boolean,
    val chatId : String? = null,
    val matchedUser : String? = null
)