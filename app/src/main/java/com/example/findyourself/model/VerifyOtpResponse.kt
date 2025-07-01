package com.example.findyourself.model

import kotlinx.serialization.Serializable

@Serializable
data class VerifyOtpResponse(
    val accessToken: String?,
    val refreshToken: String?,
    val user: User,
    val isNewUser: Boolean,
    val isOtpVerified : Boolean
)