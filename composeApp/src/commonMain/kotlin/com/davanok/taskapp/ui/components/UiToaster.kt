@file:OptIn(ExperimentalTime::class)

package com.davanok.taskapp.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dokar.sonner.TextToastAction
import com.dokar.sonner.Toast
import com.dokar.sonner.ToastType
import com.dokar.sonner.Toaster
import com.dokar.sonner.ToasterDefaults
import com.dokar.sonner.listenMany
import com.dokar.sonner.rememberToasterState
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.uuid.Uuid

private const val LOADING = "loading"

@Composable
fun UiToaster(
    messages: List<UiMessage>,
    onRemoveMessage: (id: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val toaster = rememberToasterState(
        onToastDismissed = { onRemoveMessage(it.id as Long) },
    )
    val currentMessages by rememberUpdatedState(messages)

    LaunchedEffect(toaster) {
        // Listen to State<List<UiMessage>> changes and map to toasts
        toaster.listenMany {
            currentMessages.map { message ->
                message.toToast(onDismiss = { onRemoveMessage(message.id) })
            }
        }
    }

    Toaster(
        state = toaster,
        modifier = modifier,
        richColors = true,
        darkTheme = LocalColorScheme.current.darkTheme,
        iconSlot = { toast ->
            if (toast.icon == LOADING) LoadingIcon()
            else ToasterDefaults.iconSlot(toast)
        }
    )
}
sealed interface UiMessage {
    val id: Long

    data class Success(
        val message: String,
        override val id: Long = Clock.System.now().toEpochMilliseconds(),
    ) : UiMessage

    data class Error(
        val message: String,
        override val id: Long = Clock.System.now().toEpochMilliseconds(),
        val error: Throwable? = null,
    ) : UiMessage

    data class Loading(
        val message: String,
        override val id: Long = Clock.System.now().toEpochMilliseconds(),
    ) : UiMessage
}
private fun UiMessage.toToast(onDismiss: (toast: Toast) -> Unit): Toast = when (this) {
    is UiMessage.Error -> Toast(
        id = id,
        message = message,
        type = ToastType.Error,
        duration = Duration.INFINITE,
        action = TextToastAction(text = "Dismiss", onClick = onDismiss),
    )

    is UiMessage.Success -> Toast(
        id = id,
        message = message,
        type = ToastType.Success,
        action = TextToastAction(text = "Dismiss", onClick = onDismiss),
    )

    is UiMessage.Loading -> Toast(
        id = id,
        message = message,
        type = ToastType.Normal,
        action = TextToastAction(text = "Dismiss", onClick = onDismiss),
        icon = LOADING
    )
}

@Composable
private fun LoadingIcon(modifier: Modifier = Modifier) {
    Box(modifier = modifier.padding(end = 16.dp)) {
        CircularProgressIndicator(
            modifier = Modifier.size(18.dp)
        )
    }
}