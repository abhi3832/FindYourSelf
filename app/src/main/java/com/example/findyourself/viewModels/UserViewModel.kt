package com.example.findyourself.viewModels

import androidx.lifecycle.ViewModel
import com.example.findyourself.dataClasses.User
import com.example.findyourself.dependencyInjection.SecureStorage
import com.example.findyourself.repositories.UserRepository
import com.example.findyourself.utils.SecureStorageKeys
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserViewModel @Inject constructor(
    private val userRepo: UserRepository,
) : ViewModel()
{

    fun saveUserSession(user: User, accessToken: String, refreshToken: String) {
        userRepo.saveUserData(user, accessToken, refreshToken)
    }

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    init {
        loadUser()
    }

    fun loadUser() {
        _user.value = userRepo.getUser()
    }

    fun getAccessToken(): String? = userRepo.getAccessToken()

    fun getRefreshToken(): String? = userRepo.getRefreshToken()

    fun logout() {
        userRepo.clearAll()
    }


}
