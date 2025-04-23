package com.davanok.taskapp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.davanok.taskapp.data.di.commonModule
import com.davanok.taskapp.data.di.databaseModule
import com.davanok.taskapp.data.di.platformModule
import com.davanok.taskapp.data.di.viewModelsModule
import com.davanok.taskapp.data.platform.appDeclaration
import com.davanok.taskapp.data.platform.getColorScheme
import com.davanok.taskapp.ui.components.ColorScheme
import com.davanok.taskapp.ui.components.LocalColorScheme
import com.davanok.taskapp.ui.navigation.NavGraph
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication

@Composable
@Preview
fun App() {
    KoinApplication(application = {
        appDeclaration()
        modules(commonModule(), databaseModule(), viewModelsModule(), platformModule())
    }) {
        val isDarkTheme = isSystemInDarkTheme()
        MaterialTheme (
            colorScheme = getColorScheme(isDarkTheme)
        ) {
            CompositionLocalProvider(LocalColorScheme provides ColorScheme(isDarkTheme)) {
                Surface {
                    NavGraph(
                        navController = rememberNavController(),
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}