package com.davanok.taskapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.davanok.taskapp.data.database.daos.TasksDao
import com.davanok.taskapp.data.database.entities.TaskEntity

@TypeConverters(DatabaseConverters::class)
@Database(
    entities = [
        TaskEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getTasksDao(): TasksDao
}