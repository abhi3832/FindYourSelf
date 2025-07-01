package com.example.findyourself.dependencyInjection

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import androidx.core.content.edit



@Module
object DataStoreModule {

    @Provides
    @Singleton
    fun provideDataStore(application: Application): DataStore<Preferences> =
        PreferenceDataStoreFactory.create {
            application.dataStoreFile("secure_user_prefs")
        }

    @Provides
    @Singleton
    fun provideEncryptionManager(application: Application): SecureStorage {
        return SecureStorage(application)
    }

}


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


