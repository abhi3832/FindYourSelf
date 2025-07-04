package com.example.findyourself.model

import kotlinx.serialization.Serializable

@Serializable
data class UsernameResponse(val exists: Boolean,  val error: String? = null)