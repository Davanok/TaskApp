package com.davanok.taskapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.davanok.taskapp.data.platform.appDeclaration
import com.davanok.taskapp.data.di.commonModule
import com.davanok.taskapp.data.di.databaseModule
import com.davanok.taskapp.data.di.platformModule
import com.davanok.taskapp.data.di.viewModelsModule
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication

import taskapp.composeapp.generated.resources.Res
import taskapp.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    KoinApplication(application = {
        appDeclaration()
        modules(commonModule(), databaseModule(), viewModelsModule(), platformModule())
    }) {
        MaterialTheme {
            var showContent by remember { mutableStateOf(false) }
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = { showContent = !showContent }) {
                    Text("Click me!")
                }
                AnimatedVisibility(showContent) {
                    val greeting = ""
                    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painterResource(Res.drawable.compose_multiplatform), null)
                        Text("Compose: $greeting")
                    }
                }
            }
        }
    }
}