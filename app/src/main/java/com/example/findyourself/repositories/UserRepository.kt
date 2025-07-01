package com.example.findyourself.repositories

import com.example.findyourself.dataClasses.User
import com.example.findyourself.utils.SecureStorageKeys
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserRepository @Inject constructor(
    private val secureStorage: SecureStorage
) {

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
