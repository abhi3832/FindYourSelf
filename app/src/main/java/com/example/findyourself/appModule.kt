package com.example.findyourself

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.example.findyourself.data.repositories.AuthRepositories
import com.example.findyourself.data.repositories.ConnectRepository
import com.example.findyourself.data.repositories.FirebaseConnectChatRepository
import com.example.findyourself.data.repositories.FirebaseConnectChatStatusRepository
import com.example.findyourself.data.repositories.FirebaseMessageRepository
import com.example.findyourself.data.repositories.SecureStorage
import com.example.findyourself.data.repositories.TypingStatusRepository
import com.example.findyourself.data.repositories.UserRepository
import com.example.findyourself.data.retrofit.API
import com.example.findyourself.view.viewModels.AuthViewModel
import com.example.findyourself.view.viewModels.ConnectChatViewModel
import com.example.findyourself.view.viewModels.ConnectViewModel
import com.example.findyourself.view.viewModels.MessageViewModel
import com.example.findyourself.view.viewModels.UserViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val appModule = module {
    viewModelOf(::AuthViewModel)
    viewModelOf(::UserViewModel)
    viewModelOf(::ConnectViewModel)
    viewModelOf(::ConnectChatViewModel)
    viewModelOf(::MessageViewModel)

    singleOf(::AuthRepositories)
    singleOf(::UserRepository)
    singleOf(::TypingStatusRepository)
    singleOf(::ConnectRepository)
    singleOf(::FirebaseMessageRepository)
    singleOf(::FirebaseConnectChatRepository)
    singleOf(::FirebaseConnectChatStatusRepository)
    singleOf(::SecureStorage)

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("http://192.168.29.81:8080/") //todo get from build config
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<API> { get<Retrofit>().create(API::class.java) }

    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create {
            get<Application>().dataStoreFile("secure_user_prefs")
        }
    }
}