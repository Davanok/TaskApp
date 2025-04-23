package com.davanok.taskapp.data.implementations

import com.davanok.taskapp.data.database.daos.TasksDao
import com.davanok.taskapp.data.database.entities.TaskEntity
import com.davanok.taskapp.data.repositories.TasksRepository

class TasksRepositoryImpl(
    private val dao: TasksDao
): TasksRepository {
    override suspend fun insertTask(task: TaskEntity) =
        dao.insert(task)

    override suspend fun selectAllTasks(): List<TaskEntity> =
        dao.selectAllTasks()

    override suspend fun setTaskCompleted(id: Long, completed: Boolean) =
        dao.setTaskCompleted(id, completed)

    override suspend fun getTask(id: Long) =
        dao.getTask(id)
}