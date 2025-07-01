package com.example.findyourself

import android.app.Application
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
            modules(appModule)
        }

        FirebaseApp.initializeApp(this)
    }
}