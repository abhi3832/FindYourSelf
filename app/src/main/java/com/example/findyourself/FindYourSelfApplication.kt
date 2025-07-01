package com.example.findyourself

import android.app.Application
import com.example.findyourself.dependencyInjection.ApplicationLevelComponent
import com.example.findyourself.dependencyInjection.DaggerApplicationLevelComponent
import com.google.firebase.FirebaseApp


class FindYourSelfApplication : Application() {

    lateinit var applicationLevelComponent : ApplicationLevelComponent

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        applicationLevelComponent = DaggerApplicationLevelComponent.factory().create(this)
    }
}