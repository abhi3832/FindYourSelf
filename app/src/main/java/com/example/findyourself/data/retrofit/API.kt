package com.example.findyourself.data.retrofit

import com.example.findyourself.model.ConnectRequest
import com.example.findyourself.model.ConnectResponse
import com.example.findyourself.model.RefreshTokenRequest
import com.example.findyourself.model.SendOtpResponse
import com.example.findyourself.model.SignUpRequest
import com.example.findyourself.model.SignUpResponse
import com.example.findyourself.model.TokenResponse
import com.example.findyourself.model.TokenValidityResponse
import com.example.findyourself.model.User
import com.example.findyourself.model.UsernameResponse
import com.example.findyourself.model.VerifyOtpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query


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