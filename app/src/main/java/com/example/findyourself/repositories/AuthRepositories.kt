package com.example.findyourself.repositories

import android.util.Log
import com.example.findyourself.dependencyInjection.SecureStorage
import com.example.findyourself.retrofit.API
import com.example.findyourself.retrofit.RefreshTokenRequest
import com.example.findyourself.retrofit.SendOtpResponse
import com.example.findyourself.retrofit.SignUpRequest
import com.example.findyourself.retrofit.SignUpResponse
import com.example.findyourself.retrofit.TokenResponse
import com.example.findyourself.retrofit.TokenValidityResponse
import com.example.findyourself.retrofit.UsernameResponse
import com.example.findyourself.retrofit.VerifyOtpResponse
import com.example.findyourself.utils.PreferenceKeys
import com.example.findyourself.utils.SecureStorageKeys
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositories @Inject constructor(private val api: API, private val secureStorage: SecureStorage) {

    private fun getAccessToken(): String? {
        return secureStorage.getString(SecureStorageKeys.ACCESS_TOKEN.toString())
    }

    private fun getRefreshToken(): String? {
        return secureStorage.getString(SecureStorageKeys.REFRESH_TOKEN.toString())
    }

    private fun saveTokens(accessToken: String, refreshToken: String) {
        secureStorage.saveString(SecureStorageKeys.ACCESS_TOKEN.toString(), accessToken)
        secureStorage.saveString(SecureStorageKeys.ACCESS_TOKEN.toString(), refreshToken)
    }

    suspend fun validateOrRefreshToken(): Boolean {

        val accessToken = getAccessToken() ?: return false

        val validityResult = checkTokenValidity("Bearer $accessToken")
        if (validityResult.isSuccess && validityResult.getOrNull()?.isTokenValid == true) {
            Log.d("Check123", "Valid Access Token : $accessToken")
            return true
        }

        val refreshResult = refreshToken()
        Log.d("Check123", "Access Token Expired! Refreshing Token ...")
        return if (refreshResult.isSuccess && refreshResult.getOrNull()?.refreshToken != null && refreshResult.getOrNull()?.accessToken != null) {
            Log.d("Check123", "Token Refreshed Successfully")
            true
        } else {
            Log.d("Check123", "Couldn't refresh Token !!")
            secureStorage.clear()
            false
        }
    }

    suspend fun checkTokenValidity(tokenWithBearer : String): Result<TokenValidityResponse> {
        return try{
            val response = api.checkTokenValidity(tokenWithBearer)
            if(response.isSuccessful && response.body() != null){
                Result.success(response.body()!!)
            }else{
                Result.failure(Exception("Failed to check token validity"))
            }

        }catch (e : Exception){
            Result.failure(e)
        }
    }

    suspend fun refreshToken(): Result<TokenResponse> {
        val refreshToken = getRefreshToken() ?: return Result.failure(Exception("No refresh token"))
        return try {
            val response = api.refreshTokens(refreshTokenRequest = RefreshTokenRequest(refreshToken))
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                saveTokens(body.accessToken!!, body.refreshToken!!)
                Result.success(body)
            } else {
                Result.failure(Exception("Failed to refresh token"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    fun isOnboardingCompleted(): Boolean {
        return secureStorage.getBoolean(PreferenceKeys.ONBOARDING_COMPLETED.toString())
    }

    fun setOnboardingCompleted() {
        secureStorage.saveBoolean(PreferenceKeys.ONBOARDING_COMPLETED.toString(), true)
    }

    suspend fun sendOtp(phone: String): Result<SendOtpResponse> {
        return try {
            val response = api.sendOtp(phone)
            if (response.isSuccessful && response.body() != null) {
                Log.d("Check123", "Success : ${response.body()}")
                Result.success(response.body()!!)
            } else {
                Log.d("Check123", "Failure : $${response.errorBody()?.string()}")
                Result.failure(Exception("Failed to send OTP: ${response.errorBody()?.string()}"))
            }

        } catch (e: Exception) {
            Log.d("Check123", "Error Occurred1 : $e")
            Result.failure(e)
        }
    }

    suspend fun verifyOtp(phone: String, otp : String): Result<VerifyOtpResponse> {
        return try {
            val response = api.verifyOtp(phone,otp)
            if (response.isSuccessful && response.body() != null) {
                Log.d("Check123", "Success : ${response.body()}")
                Result.success(response.body()!!)
            } else {
                Log.d("Check123", "Failure : $${response.errorBody()?.string()}")
                Result.failure(Exception("Failed to send OTP: ${response.errorBody()?.string()}"))
            }

        } catch (e: Exception) {
            Log.d("Check123", "Error Occurred1 : $e")
            Result.failure(e)
        }
    }

    suspend fun signUp(signUpRequest: SignUpRequest): Result<SignUpResponse> {
        return try {
            val response = api.signUp(signUpRequest)
            if (response.isSuccessful && response.body() != null) {
                Log.d("Check123", "Success : ${response.body()}")
                Result.success(response.body()!!)
            } else {
                Log.d("Check123", "Failure : $${response.errorBody()?.string()}")
                Result.failure(Exception("Failed to send OTP: ${response.errorBody()?.string()}"))
            }

        } catch (e: Exception) {
            Log.d("Check123", "Error Occurred1 : $e")
            Result.failure(e)
        }
    }

    suspend fun checkUsername(userName: String): Result<UsernameResponse> {
        return try {
            val response = api.checkUserName(userName)
            if (response.isSuccessful && response.body() != null) {
                Log.d("Check123", "Success : ${response.body()}")
                Result.success(response.body()!!)
            } else {
                Log.d("Check123", "Failure : $${response.errorBody()?.string()}")
                Result.failure(Exception("Failed to send OTP: ${response.errorBody()?.string()}"))
            }

        } catch (e: Exception) {
            Log.d("Check123", "Error Occurred1 : $e")
            Result.failure(e)
        }
    }

}