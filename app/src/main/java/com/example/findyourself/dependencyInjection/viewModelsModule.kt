package com.example.findyourself.dependencyInjection

import com.example.findyourself.viewModels.AuthViewModel
import com.example.findyourself.viewModels.ConnectChatViewModel
import com.example.findyourself.viewModels.ConnectViewModel
import com.example.findyourself.viewModels.MessageViewModel
import com.example.findyourself.viewModels.UserViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val viewModelsModule = module {
    viewModelOf(::AuthViewModel)
    viewModelOf(::UserViewModel)
    viewModelOf(::ConnectViewModel)
    viewModelOf(::ConnectChatViewModel)
    viewModelOf(::MessageViewModel)
}