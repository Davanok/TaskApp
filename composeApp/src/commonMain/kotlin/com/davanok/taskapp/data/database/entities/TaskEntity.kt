package com.davanok.taskapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Entity(
    tableName = "tasks"
)
data class TaskEntity(
    @PrimaryKey(true) val id: Long = 0,
    val datetime: Instant = Clock.System.now(),
    val title: String,
    val content: String,
    val tags: List<String> = emptyList(),
    val completed: Boolean = false,
)
