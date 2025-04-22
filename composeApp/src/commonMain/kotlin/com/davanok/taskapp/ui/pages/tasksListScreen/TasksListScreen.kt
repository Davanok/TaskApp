package com.davanok.taskapp.ui.pages.tasksListScreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.compose.koinInject

@Composable
fun TasksListScreen(
    onNewTask: () -> Unit,
    onShowTask: (taskId: Long) -> Unit,
    viewModel: TasksListViewModel = koinInject()
) {
    Scaffold(
        bottomBar = {
            BottomAppBar {

            }
        },
        floatingActionButton = {
            FloatingActionButton (
                onClick = onNewTask
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {

        }
    }
}