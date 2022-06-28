package com.eugenics.mapapplication.ui.screens.map.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun MarkerDialog(
    balloonText: String,
    onDismissAction: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissAction,
        dismissButton = {},
        confirmButton = {},
        title = {
            Text("Marker", style = MaterialTheme.typography.titleSmall)
        },
        text = {
            Text(text = balloonText, style = MaterialTheme.typography.bodyMedium)
        }
    )
}