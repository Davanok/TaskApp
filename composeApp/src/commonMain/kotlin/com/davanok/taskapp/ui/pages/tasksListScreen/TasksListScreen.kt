package com.davanok.taskapp.ui.pages.tasksListScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.davanok.taskapp.data.database.entities.TaskEntity
import com.davanok.taskapp.ui.components.UiToaster
import com.davanok.taskapp.ui.components.rememberBottomBarNestedScroll
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import taskapp.composeapp.generated.resources.Res
import taskapp.composeapp.generated.resources.new_task

@Composable
fun TasksListScreen(
    onNewTask: () -> Unit,
    onShowTask: (task: TaskEntity) -> Unit,
    viewModel: TasksListViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    UiToaster(
        messages = uiState.messages,
        onRemoveMessage = viewModel::hideMessage
    )
    var isBottomBarVisible by remember { mutableStateOf(true) }
    val nestedScrollConnection = rememberBottomBarNestedScroll {
        isBottomBarVisible = it
    }
    Scaffold(
        bottomBar = {
            AnimatedVisibility (
                visible = isBottomBarVisible,
                enter = slideInVertically(initialOffsetY = { it * 2 }),
                exit = slideOutVertically(targetOffsetY = { it * 2 }),
            ) {
                BottomAppBar(
                    modifier = Modifier.nestedScroll(object : NestedScrollConnection {

                    }),
                    actions = {

                    },
                    floatingActionButton = {
                        FloatingActionButton (
                            onClick = onNewTask,
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(Res.string.new_task)
                            )
                        }
                    }
                )
            }
        }
    ) { paddingValues: PaddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .nestedScroll(nestedScrollConnection)
        ) {
            items(
                items = uiState.notCompletedTasks,
                key = { it.id }
            ) {
                TasksListItem(
                    item = it,
                    onClick = onShowTask,
                    onTaskCompleted = viewModel::setTaskCompleted
                )
            }
            item (key = "divider") {
                HorizontalDivider(modifier = Modifier.fillMaxWidth())
            }
            items(
                items = uiState.completedTasks,
                key = { it.id }
            ) {
                TasksListItem(
                    item = it,
                    onClick = onShowTask,
                    onTaskCompleted = viewModel::setTaskCompleted
                )
            }
        }
    }
}

@Composable
private fun TasksListItem(
    item: TaskEntity,
    onClick: (item: TaskEntity) -> Unit,
    onTaskCompleted: (item: TaskEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        modifier = modifier
            .then(Modifier.clickable { onClick(item) }),
        headlineContent = {
            Text(
                text =
                    if (item.title.isBlank())
                        item.content.takeWhile { it != '\n' }
                    else item.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        leadingContent = {
            Checkbox(
                onCheckedChange = { onTaskCompleted(item) },
                checked = item.completed
            )
        },
    )
}