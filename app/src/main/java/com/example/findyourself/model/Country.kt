package com.example.findyourself.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Country(
    @SerialName("name")
    val name: String,

    @SerialName("dial_code")
    val dialCode: String,

    @SerialName("code")
    val code: String
)
