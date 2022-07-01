package com.eugenics.mapapplication.navigation

sealed class Screens(val route: String) {
    object Map : Screens(route = "map")
    object Markers : Screens(route = "markers")
    object Marker : Screens(route = "marker")
}
