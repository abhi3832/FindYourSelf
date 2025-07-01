package com.example.findyourself.model

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey


object PreferenceKeys {
    val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    val USER_PHONE = stringPreferencesKey("user_phone")
}

