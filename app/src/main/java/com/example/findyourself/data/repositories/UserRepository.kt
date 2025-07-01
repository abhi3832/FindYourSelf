package com.example.findyourself.data.repositories

import com.example.findyourself.model.User
import com.example.findyourself.model.SecureStorageKeys
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserRepository: KoinComponent {
    private val secureStorage: SecureStorage by inject()

    fun saveUserData(user: User, accessToken: String, refreshToken: String) {
        val userJson = Json.encodeToString(user)
        secureStorage.saveString(SecureStorageKeys.USER_DATA.toString(), userJson)
        secureStorage.saveString(SecureStorageKeys.ACCESS_TOKEN.toString(), accessToken)
        secureStorage.saveString(SecureStorageKeys.REFRESH_TOKEN.toString(), refreshToken)
    }

    fun getUser(): User? {
        val userJson = secureStorage.getString(SecureStorageKeys.USER_DATA.toString())
        return userJson?.let { Json.decodeFromString<User>(it) }
    }

    fun getAccessToken(): String? = secureStorage.getString(SecureStorageKeys.ACCESS_TOKEN.toString())

    fun getRefreshToken(): String? = secureStorage.getString(SecureStorageKeys.REFRESH_TOKEN.toString())

    fun clearAll() = secureStorage.clear()
}
