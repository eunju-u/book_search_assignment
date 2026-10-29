package com.example.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ErrorHost(
    hasError: Boolean,
    message: String,
    onErrorShown: () -> Unit,
    content: @Composable () -> Unit,
) {
    val hostState = remember { SnackbarHostState() }
    val currentOnErrorShown = rememberUpdatedState(onErrorShown)

    LaunchedEffect(hasError, message) {
        if (hasError) {
            hostState.showSnackbar(message)
            currentOnErrorShown.value()
        }
    }

    Box(Modifier.fillMaxSize()) {
        content()
        SnackbarHost(hostState, Modifier.align(Alignment.BottomCenter))
    }
}
