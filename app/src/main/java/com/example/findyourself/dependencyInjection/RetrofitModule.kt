package com.example.findyourself.dependencyInjection

import com.example.findyourself.retrofit.API
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RetrofitModule {

    @Provides
    @Singleton
    fun getRetrofit() : Retrofit {
        return Retrofit.Builder().baseUrl("http://192.168.29.81:8080/").addConverterFactory(
            GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun getQuotesApi(retrofit: Retrofit) : API {
        return retrofit.create(API :: class.java)
    }

}