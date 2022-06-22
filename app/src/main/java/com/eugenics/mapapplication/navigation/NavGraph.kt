package com.eugenics.mapapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eugenics.mapapplication.ui.screens.map.MapScreen

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "map"
    ) {
        composable(route = Screens.Map.route) { MapScreen() }
        composable(route = Screens.Markers.route) {}
    }
}