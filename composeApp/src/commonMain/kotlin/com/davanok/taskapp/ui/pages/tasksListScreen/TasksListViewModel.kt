package com.davanok.taskapp.ui.pages.tasksListScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davanok.taskapp.data.database.entities.TaskEntity
import com.davanok.taskapp.data.repositories.TasksRepository
import com.davanok.taskapp.ui.components.UiMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TasksListViewModel (
    private val repository: TasksRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TasksListUiState())
    fun loadTasks() = viewModelScope.launch {

    }
}

data class TasksListUiState(
    val messages: List<UiMessage> = emptyList(),
    val tasks: List<TaskEntity> = emptyList()
)