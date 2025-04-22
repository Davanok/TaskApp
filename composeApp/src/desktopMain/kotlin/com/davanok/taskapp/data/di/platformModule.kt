package com.davanok.taskapp.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.davanok.taskapp.data.database.AppDatabase
import com.davanok.taskapp.data.platform.appDataDirectory
import org.koin.dsl.module

actual fun platformModule() = module {
    factory {
        Room.databaseBuilder<AppDatabase>(
            name = (appDataDirectory() / "database" / "database.db").toString()
        )
    }
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.createWithPath(
            produceFile = {
                appDataDirectory() / "database" / "preferences.preferences_pb"
            }
        )
    }
}