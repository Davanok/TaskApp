package com.davanok.taskapp.data.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.davanok.taskapp.data.database.AppDatabase
import com.davanok.taskapp.data.implementations.TasksRepositoryImpl
import com.davanok.taskapp.data.repositories.TasksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module

fun databaseModule() = module {
    single<AppDatabase> {
        get<RoomDatabase.Builder<AppDatabase>>()
            .fallbackToDestructiveMigration(true)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
    single { get<AppDatabase>().getTasksDao() }

    single<TasksRepository> { TasksRepositoryImpl(get()) }
}