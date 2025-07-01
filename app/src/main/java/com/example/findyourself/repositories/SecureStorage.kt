package com.example.findyourself.repositories

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecureStorage(context: Context) {

    private val masterKeyAlias = MasterKey.DEFAULT_MASTER_KEY_ALIAS
    private val masterKey = MasterKey.Builder(context, masterKeyAlias)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val cipher: SharedPreferences = EncryptedSharedPreferences.create(
        "secure_user_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveString(key: String, value: String) {
        cipher.edit { putString(key, value) }
    }

    fun saveBoolean(key: String, value: Boolean) {
        cipher.edit { putBoolean(key, value) }
    }

    fun getBoolean(key: String): Boolean {
        return cipher.getBoolean(key, false)
    }


    // Retrieve a string securely by key
    fun getString(key: String): String? {
        return cipher.getString(key, null)
    }

    // Clear all stored data (optional)
    fun clear() {
        cipher.edit { clear() }
    }
}