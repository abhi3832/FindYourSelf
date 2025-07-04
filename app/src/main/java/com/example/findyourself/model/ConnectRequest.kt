package com.example.findyourself.model

import kotlinx.serialization.Serializable

@Serializable
data class ConnectRequest(
    val uid : String,
    val interests : List<String>
)