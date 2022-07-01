package com.eugenics.mapapplication.ui.screens.map

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.eugenics.mapapplication.R
import com.eugenics.mapapplication.domain.model.MapMarker
import com.eugenics.mapapplication.navigation.Screens
import com.eugenics.mapapplication.ui.screens.map.components.TomMap
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.flow.StateFlow


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    navController: NavHostController,
    markers: StateFlow<List<MapMarker>>,
    setMarkerHandler: (mapMarker: MapMarker) -> Unit = { _ -> },
    saveMarkerHandler: (List<MapMarker>) -> Unit = { _ -> }
) {
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.map),
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(5.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Filled.Menu, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(Screens.Markers.route)
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.Place, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        if (locationPermissionState.status == PermissionStatus.Granted) {
            TomMap(
                navController = navController,
                paddingValues = paddingValues,
                markers = markers,
                setMarkerHandler = setMarkerHandler,
                saveMarkerHandler = saveMarkerHandler
            )
        } else {
            Column {
                val textToShow = if (locationPermissionState.status.shouldShowRationale) {
                    stringResource(R.string.grant_request_info)
                } else {
                    stringResource(R.string.grant_requirement_info)
                }
                Text(textToShow)
                Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
                    Text(stringResource(R.string.request_premissions))
                }
            }
        }
    }
}