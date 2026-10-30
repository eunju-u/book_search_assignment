package com.example.designsystem.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun PopupWidget(
    message: String,
    onDismiss: () -> Unit,
    confirmText: String? = null,
    cancelText: String? = null,
    onConfirm: () -> Unit = onDismiss,
    onCancel: () -> Unit = onDismiss,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = { Text(message) },
        confirmButton = {
            if (confirmText != null) {
                TextButton(onClick = onConfirm) { Text(confirmText) }
            }
        },
        dismissButton = {
            if (cancelText != null) {
                TextButton(onClick = onCancel) { Text(cancelText) }
            }
        },
    )
}