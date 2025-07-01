package com.example.findyourself.dataClasses

import kotlinx.serialization.Serializable

@Serializable
data class ConnectRequest(
    val uid : String,
    val interests : List<String>
)

@Serializable
data class ConnectResponse(
    val isConnected : Boolean,
    val chatId : String? = null,
    val matchedUser : String? = null
)