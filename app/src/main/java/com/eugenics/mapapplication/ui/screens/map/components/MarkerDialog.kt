package com.eugenics.mapapplication.ui.screens.map.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.eugenics.mapapplication.R

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
            Text(stringResource(R.string.marker), style = MaterialTheme.typography.titleSmall)
        },
        text = {
            Text(text = balloonText, style = MaterialTheme.typography.bodyMedium)
        }
    )
}

@Composable
fun MarkerTitleDialog(
    title: String = "",
    onDismissAction: () -> Unit,
    onConfirm: (title: String) -> Unit
) {
    val text = remember { mutableStateOf(title) }

    AlertDialog(
        onDismissRequest = onDismissAction,
        dismissButton = {},
        confirmButton = {
            Button(onClick = { onConfirm(text.value) }) {
                Text(
                    text = stringResource(R.string.save),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        },
        title = {
            Text(stringResource(R.string.marker), style = MaterialTheme.typography.titleSmall)
        },
        text = {
            OutlinedTextField(
                value = text.value,
                onValueChange = { text.value = it },
                label = { Text(text = stringResource(R.string.title)) }
            )
        }
    )
}