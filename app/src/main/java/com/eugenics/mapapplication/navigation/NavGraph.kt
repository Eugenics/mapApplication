package com.eugenics.mapapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eugenics.mapapplication.ui.screens.map.MapScreen
import com.eugenics.mapapplication.ui.screens.map.MapViewModel
import com.eugenics.mapapplication.ui.screens.markers.MarkerScreen
import com.eugenics.mapapplication.ui.screens.markers.MarkersScreen


@Composable
fun NavGraph(
    navController: NavHostController
) {
    val path = "${LocalContext.current.filesDir}/data/markers.json"
    val mapViewModel = MapViewModel.newInstance(dataFilePath = path)

    NavHost(
        navController = navController,
        startDestination = "map"
    ) {
        composable(route = Screens.Map.route) {
            MapScreen(
                navController = navController,
                markers = mapViewModel.markers,
                setMarkerHandler = { mapMarker -> mapViewModel.setMarker(mapMarker) },
                saveMarkerHandler = { markers ->
                    mapViewModel.writeMarkers(markers = markers)
                }
            )
        }
        composable(route = Screens.Markers.route) {
            MarkersScreen(
                navController = navController,
                markers = mapViewModel.markers,
                saveMarkerHandler = { markers ->
                    mapViewModel.writeMarkers(markers = markers)
                }
            )
        }
        composable(route = Screens.Marker.route) {
            MarkerScreen(
                navController = navController,
                marker = mapViewModel.marker.value,
                saveMarkerHandler = { mapMarker -> mapViewModel.saveMarker(mapMarker) }
            )
        }
    }
}