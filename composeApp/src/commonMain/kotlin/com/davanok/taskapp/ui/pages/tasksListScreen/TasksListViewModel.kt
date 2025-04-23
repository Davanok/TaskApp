package com.davanok.taskapp.ui.pages.tasksListScreen

import androidx.compose.ui.util.fastFilter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davanok.taskapp.data.database.entities.TaskEntity
import com.davanok.taskapp.data.repositories.TasksRepository
import com.davanok.taskapp.ui.components.UiMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import taskapp.composeapp.generated.resources.Res
import taskapp.composeapp.generated.resources.fail_when_load
import taskapp.composeapp.generated.resources.loading

class TasksListViewModel(
    private val repository: TasksRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(TasksListUiState())
    val uiState: StateFlow<TasksListUiState> = _uiState

    private var tasks: List<TaskEntity> = emptyList()

    fun loadTasks() = viewModelScope.launch {
        val message = UiMessage.Loading(getString(Res.string.loading))
        _uiState.value = _uiState.value.run {
            copy(messages = messages + message)
        }
        runCatching {
            repository.selectAllTasks()
        }.onFailure {
            _uiState.value = _uiState.value.run {
                copy(
                    messages = messages
                        .fastFilter { it.id != message.id } +
                            UiMessage.Error(getString(Res.string.fail_when_load), error = it)
                )
            }
        }.onSuccess { loadedTasks ->
            tasks = loadedTasks
            _uiState.value = _uiState.value.run {
                val (completed, notCompleted) = loadedTasks.partition { it.completed }
                copy(
                    completedTasks = completed,
                    notCompletedTasks = notCompleted,
                    messages = messages.fastFilter { it.id != message.id }
                )
            }
        }
    }
    fun hideMessage(messageId: Long) {
        _uiState.value = _uiState.value.run {
            copy(messages = messages.fastFilter { it.id != messageId })
        }
    }
    fun setTaskCompleted(task: TaskEntity) = viewModelScope.launch {
        val changedTask = task.copy(completed = !task.completed)
        repository.setTaskCompleted(task.id, changedTask.completed)
        _uiState.value = _uiState.value.run {
            if (task.completed)
                copy(
                    completedTasks = completedTasks.fastFilter { it.id != task.id },
                    notCompletedTasks = notCompletedTasks + changedTask
                )
            else
                copy(
                    completedTasks = completedTasks + changedTask,
                    notCompletedTasks = notCompletedTasks.fastFilter { it.id != task.id }
                )
        }
    }

    fun setSearchQuery(value: String) {
        _uiState.value = _uiState.value.copy(searchQuery = value)
        val filteredTasks = tasks.fastFilter {
            it.title.startsWith(value)
        }
        val (completed, notCompleted) = filteredTasks.partition { it.completed }
        _uiState.value = _uiState.value.copy(
            completedTasks = completed,
            notCompletedTasks = notCompleted,
        )
    }

    init {
        loadTasks()
    }
}

data class TasksListUiState(
    val messages: List<UiMessage> = emptyList(),
    val completedTasks: List<TaskEntity> = emptyList(),
    val notCompletedTasks: List<TaskEntity> = emptyList(),
    val searchQuery: String = ""
)