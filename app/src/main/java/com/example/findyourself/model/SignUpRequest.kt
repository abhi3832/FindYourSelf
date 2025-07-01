package com.example.findyourself.model

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val fullName: String,
    val username: String,
    val phoneNumber: String,
    val gender: String,
    val profilePhotoUrl: String? = null,
    val age: Int,
    val about: String
)