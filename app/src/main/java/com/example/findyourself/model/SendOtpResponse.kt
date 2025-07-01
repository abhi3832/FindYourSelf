package com.example.findyourself.model

import kotlinx.serialization.Serializable

@Serializable
data class SendOtpResponse(
    val success: Boolean,
    val message: String,
)