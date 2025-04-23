package com.davanok.taskapp.ui.pages.tasksListScreen

import androidx.compose.ui.util.fastFilter
import androidx.compose.ui.util.fastFlatMap
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

class TasksListViewModel(
    private val repository: TasksRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(TasksListUiState())
    val uiState: StateFlow<TasksListUiState> = _uiState

    private var tasks: List<TaskEntity> = emptyList()
    private var allTags: List<String> = emptyList()

    fun loadTasks() = viewModelScope.launch {
        runCatching {
            repository.selectAllTasks()
        }.onFailure {
            _uiState.value = _uiState.value.run {
                copy(
                    messages = messages +
                            UiMessage.Error(getString(Res.string.fail_when_load), error = it)
                )
            }
        }.onSuccess { loadedTasks ->
            tasks = loadedTasks
            allTags = loadedTasks.fastFlatMap { it.tags }.distinct()
            _uiState.value = _uiState.value.run {
                val (completed, notCompleted) = tasks.partition { it.completed }
                copy(
                    completedTasks = completed,
                    notCompletedTasks = notCompleted,
                    filteredTags = allTags
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
        val (completed, notCompleted) =
            (if (value.isBlank()) tasks else tasks.fastFilter { it.title.startsWith(value) || value in it.tags })
            .partition { it.completed }
        _uiState.value = _uiState.value.copy(
            completedTasks = completed,
            notCompletedTasks = notCompleted,
            filteredTags = allTags.fastFilter { it.startsWith(value) }.distinct()
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
    val searchQuery: String = "",
    val filteredTags: List<String> = emptyList()
)