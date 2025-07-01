package com.example.findyourself.model

import androidx.datastore.preferences.core.stringPreferencesKey

object SecureStorageKeys {
    val ACCESS_TOKEN = stringPreferencesKey("access_token")
    val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    val USER_DATA = stringPreferencesKey("user_data")
}