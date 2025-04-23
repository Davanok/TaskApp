package com.davanok.taskapp.ui.pages.taskInfoScreen

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

class TaskInfoViewModel(
    private val repository: TasksRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TaskInfoUiState())
    val uiState: StateFlow<TaskInfoUiState> = _uiState

    fun loadTask(taskId: Long?) = viewModelScope.launch {
        runCatching {
            if (taskId == null) TaskEntity()
            else repository.getTask(taskId)
        }.onFailure {
            _uiState.value = _uiState.value.run {
                copy(
                    messages = messages +
                            UiMessage.Error(getString(Res.string.fail_when_load), error = it)
                )
            }
        }.onSuccess { loadedTask ->
            _uiState.value = _uiState.value.run {
                copy(task = loadedTask?: TaskEntity())
            }
        }
    }

    fun hideMessage(messageId: Long) {
        _uiState.value = _uiState.value.run {
            copy(messages = messages.fastFilter { it.id != messageId })
        }
    }

    fun saveTask(onSuccess: () -> Unit) = viewModelScope.launch {
        repository.insertTask(_uiState.value.task)
    }.invokeOnCompletion { onSuccess() }

    fun setTaskTitle(value: String) {
        _uiState.value = _uiState.value.run {
            copy(task = task.copy(title = value))
        }
    }
    fun setTaskContent(value: String) {
        _uiState.value = _uiState.value.run {
            copy(task = task.copy(content = value))
        }
    }
    fun addTaskTag(tag: String) {
        _uiState.value = _uiState.value.run {
            copy(task = task.copy(tags = task.tags + tag))
        }
    }
    fun removeTaskTag(tag: String) {
        _uiState.value = _uiState.value.run {
            copy(task = task.copy(tags = task.tags.fastFilter { it != tag }))
        }
    }
}
data class TaskInfoUiState(
    val messages: List<UiMessage> = emptyList(),
    val task: TaskEntity = TaskEntity()
)