package com.example.findyourself.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenValidityResponse(
    val isTokenValid : Boolean,
    val message : String? = null,
)