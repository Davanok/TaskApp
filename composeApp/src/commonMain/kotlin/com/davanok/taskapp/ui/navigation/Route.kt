package com.davanok.taskapp.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable data object TasksList: Route
    @Serializable data class EditTask(val taskId: Long?): Route
}