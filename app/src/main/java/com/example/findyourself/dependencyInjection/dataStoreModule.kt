package com.example.findyourself.dependencyInjection

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.example.findyourself.repositories.SecureStorage
import org.koin.dsl.module


val dataStoreModule = module {
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create {
            get<Application>().dataStoreFile("secure_user_prefs")
        }
    }

    single<SecureStorage> {
        SecureStorage(get<Application>())
    }
}
