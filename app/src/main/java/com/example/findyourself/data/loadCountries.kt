package com.example.findyourself.data

import android.content.Context
import com.example.findyourself.model.Country
import kotlinx.serialization.json.Json


fun loadCountries(context: Context): List<Country> {
    val json = context.assets.open("countries.json")
        .bufferedReader()
        .use { it.readText() }

    return Json {
        ignoreUnknownKeys = true
    }.decodeFromString(json)
}