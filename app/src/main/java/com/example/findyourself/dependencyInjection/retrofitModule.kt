package com.example.findyourself.dependencyInjection

import com.example.findyourself.retrofit.API
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val retrofitModule = module {
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("http://192.168.29.81:8080/") //todo get from build config
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<API> { get<Retrofit>().create(API::class.java) }
}