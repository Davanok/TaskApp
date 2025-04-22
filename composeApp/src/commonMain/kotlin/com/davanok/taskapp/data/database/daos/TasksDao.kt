package com.davanok.taskapp.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.davanok.taskapp.data.database.entities.TaskEntity

@Dao
interface TasksDao {
    @Insert
    suspend fun insert(task: TaskEntity)

    @Query("SELECT * FROM tasks")
    suspend fun selectAllTasks(): List<TaskEntity>

    @Query("UPDATE tasks SET completed = :completed WHERE id == :id")
    suspend fun setTaskCompleted(id: Long, completed: Boolean)
}