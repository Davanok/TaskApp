package com.davanok.taskapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.davanok.taskapp.ui.pages.tasksListScreen.TasksListScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Route.TasksList
    ) {
        composable<Route.TasksList> {
            TasksListScreen(
                onNewTask = { navController.navigate(Route.EditTask(null)) },
                onShowTask = { navController.navigate(Route.EditTask(it)) }
            )
        }
        composable<Route.EditTask> { backStack ->
            val route: Route.EditTask = backStack.toRoute()
        }
    }
}