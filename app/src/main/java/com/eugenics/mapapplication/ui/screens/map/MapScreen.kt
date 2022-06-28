package com.eugenics.mapapplication.ui.screens.map

import android.Manifest
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.fragment.app.FragmentManager
import com.eugenics.mapapplication.ui.screens.map.components.TomMap
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    context: Context,
    fragmentManager: FragmentManager
) {
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    Scaffold { paddingValues ->
        if (locationPermissionState.status == PermissionStatus.Granted) {
            TomMap(
                paddingValues = paddingValues,
                supportFragmentManager = fragmentManager,
                context = context
            )
        } else {
            Column {
                val textToShow = if (locationPermissionState.status.shouldShowRationale) {
                    "The location data is important for this app. Please grant the permission."
                } else {
                    "Location permission required for this feature to be available. " +
                            "Please grant the permission"
                }
                Text(textToShow)
                Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
                    Text("Request permission")
                }
            }
        }
    }
}