package com.example.findyourself.model

import kotlinx.serialization.Serializable

@Serializable
data class SignUpResponse(
    val accessToken: String?,
    val refreshToken: String?,
    val user: User?,
    val isOtpVerified : Boolean
)