package com.example.findyourself.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val uid: String,
    val name: String,
    val phone: String,
    val profileImageUrl: String? = null,
    val aboutYourself: String? = null,
    val username : String,
    val gender: String,
    val age: Int,
    val isOnline: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val pushToken: String? = null
)