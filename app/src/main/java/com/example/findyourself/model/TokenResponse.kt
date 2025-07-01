package com.example.findyourself.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val accessToken: String?,
    val refreshToken: String?
)