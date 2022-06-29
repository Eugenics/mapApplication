package com.eugenics.mapapplication.ui.screens.markers

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkersScreen(
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(text = "Markers", style = MaterialTheme.typography.titleSmall)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                })
        }
    ) {
        Text(
            text = "Markers",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(10.dp)
        )
    }
}