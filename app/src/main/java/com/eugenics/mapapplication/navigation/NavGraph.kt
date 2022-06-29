package com.eugenics.mapapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eugenics.mapapplication.ui.screens.map.MapScreen
import com.eugenics.mapapplication.ui.screens.markers.MarkersScreen


@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "map"
    ) {
        composable(route = Screens.Map.route) {
            MapScreen(
                navController = navController
            )
        }
        composable(route = Screens.Markers.route) {
            MarkersScreen(
                navController = navController
            )
        }
    }
}