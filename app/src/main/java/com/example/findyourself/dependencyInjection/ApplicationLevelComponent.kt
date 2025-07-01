package com.example.findyourself.dependencyInjection

import android.app.Application
import android.content.Context
import com.example.findyourself.MainActivity
import com.example.findyourself.retrofit.API
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Singleton
@Component(modules = [RetrofitModule :: class, ViewModelModule :: class, DataStoreModule :: class, FirebaseModule :: class])
interface ApplicationLevelComponent{

    fun inject(mainActivity: MainActivity)

    fun getApi() : API


    @Component.Factory
    interface Factory{
        fun create(@BindsInstance application: Application) : ApplicationLevelComponent
    }
}