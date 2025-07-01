package com.example.findyourself.retrofit

import com.example.findyourself.dataClasses.ConnectRequest
import com.example.findyourself.dataClasses.ConnectResponse
import com.example.findyourself.dataClasses.User
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

@Serializable
data class SendOtpResponse(
    val success: Boolean,
    val message: String,
)


@Serializable
data class UsernameRequest(val username: String)

@Serializable
data class UsernameResponse(val exists: Boolean,  val error: String? = null)


@Serializable
data class VerifyOtpResponse(
    val accessToken: String?,
    val refreshToken: String?,
    val user: User,
    val isNewUser: Boolean,
    val isOtpVerified : Boolean
)

@Serializable
data class SignUpResponse(
    val accessToken: String?,
    val refreshToken: String?,
    val user: User?,
    val isOtpVerified : Boolean
)

@Serializable
data class SignUpRequest(
    val fullName: String,
    val username: String,
    val phoneNumber: String,
    val gender: String,
    val profilePhotoUrl: String? = null,
    val age: Int,
    val about: String
)

@Serializable
data class TokenValidityResponse(
    val isTokenValid : Boolean,
    val message : String? = null,
)

@Serializable
data class RefreshTokenRequest(
    val refreshToken: String
)

@Serializable
data class TokenResponse(
    val accessToken: String?,
    val refreshToken: String?
)


interface API {

    @POST("sendOtp")
    suspend fun sendOtp(@Query("phone") phoneNumber : String) : Response<SendOtpResponse>

    @POST("verifyOtp")
    suspend fun verifyOtp(@Query("phone") phoneNumber : String, @Query("otp") otp : String) : Response<VerifyOtpResponse>

    @POST("checkUserName")
    suspend fun checkUserName(@Query("userName") userName : String) : Response<UsernameResponse>

    @POST("signUp")
    suspend fun signUp(@Body signUpRequest: SignUpRequest) : Response<SignUpResponse>

    @POST("checkTokenValidity")
    suspend fun checkTokenValidity(@Header("Authorization") token: String): Response<TokenValidityResponse>

    @POST("refreshTokens")
    suspend fun refreshTokens(@Body refreshTokenRequest: RefreshTokenRequest): Response<TokenResponse>

    @POST("connect")
    suspend fun connectToStranger(@Body connectRequest: ConnectRequest): Response<ConnectResponse>

    @GET("userDetails")
    suspend fun getChatUser(@Query("userId") userId: String): Response<User>


}