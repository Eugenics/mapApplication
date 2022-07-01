package com.eugenics.mapapplication.ui.screens.markers

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.eugenics.mapapplication.R
import com.eugenics.mapapplication.domain.model.MapMarker
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MarkersScreen(
    navController: NavHostController,
    markers: StateFlow<List<MapMarker>>,
    saveMarkerHandler: (List<MapMarker>) -> Unit = { _ -> }
) {
    val mapMarkers = mutableListOf<MapMarker>().apply {
        this.addAll(markers.collectAsState().value)
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.markers),
                        style = MaterialTheme.typography.titleSmall
                    )
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
        val state = rememberLazyListState()
        LazyColumn(
            state = state,
            contentPadding = it,
            verticalArrangement = Arrangement.spacedBy(5.dp),
            content = {
                items(
                    mapMarkers,
                    key = { item -> item.text }
                ) { marker ->
                    Row(
                        Modifier.animateItemPlacement(tween(durationMillis = 500)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Place,
                            contentDescription = null,
                            modifier = Modifier.padding(5.dp).weight(weight = 0.3F)
                        )
                        Text(
                            text = marker.text,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(5.dp).weight(weight = 1F)
                        )
                        IconButton(
                            onClick = {
                                mapMarkers.remove(marker)
                                saveMarkerHandler(mapMarkers)
                            },
                            modifier = Modifier.padding(5.dp).weight(weight = 0.3F)
                        ) {
                            Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
                        }
                    }
                    Divider(
                        color = MaterialTheme.colorScheme.onSurface,
                        thickness = 1.dp,
                        modifier = Modifier.padding(start = 5.dp, end = 5.dp)
                    )
                }
            }
        )
    }
}