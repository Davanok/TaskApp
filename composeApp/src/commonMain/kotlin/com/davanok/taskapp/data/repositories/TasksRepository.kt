package com.davanok.taskapp.data.repositories

import com.davanok.taskapp.data.database.entities.TaskEntity

interface TasksRepository {
    suspend fun insertTask(task: TaskEntity)
    suspend fun selectAllTasks(): List<TaskEntity>
    suspend fun setTaskCompleted(id: Long, completed: Boolean)
    suspend fun getTask(id: Long): TaskEntity?
}