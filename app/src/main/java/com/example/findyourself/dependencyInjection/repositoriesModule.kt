package com.example.findyourself.dependencyInjection

import com.example.findyourself.repositories.AuthRepositories
import com.example.findyourself.repositories.ConnectRepository
import com.example.findyourself.repositories.FirebaseConnectChatRepository
import com.example.findyourself.repositories.FirebaseConnectChatStatusRepo
import com.example.findyourself.repositories.FirebaseMessageRepository
import com.example.findyourself.repositories.SecureStorage
import com.example.findyourself.repositories.TypingStatusRepository
import com.example.findyourself.repositories.UserRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoriesModule = module {
    singleOf(::AuthRepositories)
    singleOf(::UserRepository)
    singleOf(::TypingStatusRepository)
    singleOf(::ConnectRepository)
    singleOf(::FirebaseMessageRepository)
    singleOf(::FirebaseConnectChatRepository)
    singleOf(::FirebaseConnectChatStatusRepo)
    singleOf(::SecureStorage)
}