package com.eugenics.mapapplication.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eugenics.mapapplication.ui.screens.map.MapScreen


@Composable
fun NavGraph(
    navController: NavHostController,
    context: Context,
    fragmentManager: FragmentManager
) {
    NavHost(
        navController = navController,
        startDestination = "map"
    ) {
        composable(route = Screens.Map.route) {
            MapScreen(
                context = context,
                fragmentManager = fragmentManager
            )
        }
        composable(route = Screens.Markers.route) {}
    }
}