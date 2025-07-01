package com.example.findyourself

import android.app.Application
import com.example.findyourself.dependencyInjection.dataStoreModule
import com.example.findyourself.dependencyInjection.repositoriesModule
import com.example.findyourself.dependencyInjection.retrofitModule
import com.example.findyourself.dependencyInjection.viewModelsModule
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module


class FindYourSelfApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger()
            androidContext(this@FindYourSelfApplication)
            module { single<Application> { this@FindYourSelfApplication } }
            modules(retrofitModule)
            modules(dataStoreModule)
            modules(viewModelsModule)
            modules(repositoriesModule)
        }

        FirebaseApp.initializeApp(this)
    }
}