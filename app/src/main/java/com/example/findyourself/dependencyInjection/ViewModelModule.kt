package com.example.findyourself.dependencyInjection

import androidx.lifecycle.ViewModel
import com.example.findyourself.viewModels.AuthViewModel
import com.example.findyourself.viewModels.ConnectChatViewModel
import com.example.findyourself.viewModels.ConnectViewModel
import com.example.findyourself.viewModels.MessageViewModel
import com.example.findyourself.viewModels.UserViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {

    @Binds
    @ClassKey(AuthViewModel::class)
    @IntoMap
    abstract fun authViewModel(authViewModel: AuthViewModel): ViewModel

    @Binds
    @ClassKey(UserViewModel::class)
    @IntoMap
    abstract fun userViewModel(userViewModel: UserViewModel): ViewModel

    @Binds
    @ClassKey(ConnectViewModel::class)
    @IntoMap
    abstract fun connectViewModel(connectViewModel: ConnectViewModel): ViewModel

    @Binds
    @ClassKey(ConnectChatViewModel::class)
    @IntoMap
    abstract fun connectChatViewModel(connectChatViewModel: ConnectChatViewModel): ViewModel

    @Binds
    @ClassKey(MessageViewModel::class)
    @IntoMap
    abstract fun messageViewModel(messageViewModel: MessageViewModel): ViewModel

}