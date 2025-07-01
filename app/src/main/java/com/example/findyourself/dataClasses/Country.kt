package com.example.findyourself.dataClasses

import android.content.Context
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Country(
    @SerialName("name")
    val name: String,

    @SerialName("dial_code")
    val dialCode: String,

    @SerialName("code")
    val code: String
)

fun loadCountries(context: Context): List<Country> {
    val json = context.assets.open("countries.json")
        .bufferedReader()
        .use { it.readText() }

    return Json {
        ignoreUnknownKeys = true
    }.decodeFromString(json)
}
