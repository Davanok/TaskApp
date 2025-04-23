package com.davanok.taskapp.data.di

import com.davanok.taskapp.ui.pages.taskInfoScreen.TaskInfoViewModel
import com.davanok.taskapp.ui.pages.tasksListScreen.TasksListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun viewModelsModule() = module {
    viewModelOf(::TasksListViewModel)
    viewModelOf(::TaskInfoViewModel)
}