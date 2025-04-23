package com.davanok.taskapp.ui.pages.taskInfoScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.davanok.taskapp.ui.components.UiToaster
import com.davanok.taskapp.ui.components.rememberBottomBarNestedScroll
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import taskapp.composeapp.generated.resources.Res
import taskapp.composeapp.generated.resources.add_tag
import taskapp.composeapp.generated.resources.back_to_tasks_list
import taskapp.composeapp.generated.resources.content
import taskapp.composeapp.generated.resources.remove_tag
import taskapp.composeapp.generated.resources.save_task
import taskapp.composeapp.generated.resources.tags
import taskapp.composeapp.generated.resources.title

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TaskInfoScreen(
    taskId: Long?,
    navigateToTasksList: () -> Unit,
    viewModel: TaskInfoViewModel = koinInject()
) {
    LaunchedEffect(taskId) {
        viewModel.loadTask(taskId)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    UiToaster(
        messages = uiState.messages,
        onRemoveMessage = viewModel::hideMessage
    )
    var isBottomBarVisible by remember { mutableStateOf(true) }
    val nestedScrollConnection = rememberBottomBarNestedScroll {
        isBottomBarVisible = it
    }
    Scaffold (
        bottomBar = {
            AnimatedVisibility (
                visible = isBottomBarVisible,
                enter = slideInVertically(initialOffsetY = { it * 2 }),
                exit = slideOutVertically(targetOffsetY = { it * 2 }),
            ) {
                BottomAppBar(
                    actions = {
                        IconButton (
                            onClick = navigateToTasksList
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = stringResource(Res.string.back_to_tasks_list)
                            )
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton (
                            onClick = { viewModel.saveTask(navigateToTasksList) },
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = stringResource(Res.string.save_task)
                            )
                        }
                    }
                )
            }
        }
    ) {
        Column (
            modifier = Modifier
                .safeDrawingPadding()
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .nestedScroll(nestedScrollConnection),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.task.title,
                onValueChange = viewModel::setTaskTitle,
                singleLine = true,
                label = { Text(text = stringResource(Res.string.title)) }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.task.content,
                onValueChange = viewModel::setTaskContent,
                label = { Text(text = stringResource(Res.string.content)) }
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                uiState.task.tags.fastForEach { tag ->
                    AssistChip(
                        onClick = { viewModel.removeTaskTag(tag) },
                        label = { Text(tag) },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(Res.string.remove_tag)
                            )
                        }
                    )
                }
            }
            var newTagText by remember { mutableStateOf("") }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = newTagText,
                onValueChange = { newTagText = it },
                trailingIcon = {
                    IconButton (
                        onClick = {
                            if (newTagText.isNotBlank()) {
                                viewModel.addTaskTag(newTagText)
                                newTagText = ""
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(Res.string.add_tag)
                        )
                    }
                },
                singleLine = true,
                label = { Text(text = stringResource(Res.string.tags)) }
            )
        }
    }
}