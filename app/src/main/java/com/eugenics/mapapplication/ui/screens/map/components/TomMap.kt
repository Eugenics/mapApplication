package com.eugenics.mapapplication.ui.screens.map.components

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.eugenics.mapapplication.BuildConfig
import com.eugenics.mapapplication.domain.model.MapMarker
import com.eugenics.mapapplication.domain.util.Converter
import com.eugenics.mapapplication.navigation.Screens
import com.tomtom.sdk.common.location.GeoCoordinate
import com.tomtom.sdk.common.measures.Distance
import com.tomtom.sdk.common.time.Duration
import com.tomtom.sdk.location.LocationEngine
import com.tomtom.sdk.location.android.AndroidLocationEngine
import com.tomtom.sdk.location.android.AndroidLocationEngineConfig
import com.tomtom.sdk.maps.display.MapOptions
import com.tomtom.sdk.maps.display.camera.CameraOptions
import com.tomtom.sdk.maps.display.image.ImageFactory
import com.tomtom.sdk.maps.display.location.LocationMarkerOptions
import com.tomtom.sdk.maps.display.location.LocationMarkerType
import com.tomtom.sdk.maps.display.marker.Marker
import com.tomtom.sdk.maps.display.marker.MarkerOptions
import com.tomtom.sdk.maps.display.ui.MapView
import kotlinx.coroutines.flow.StateFlow

private val androidLocationEngineConfig = AndroidLocationEngineConfig(
    minTimeInterval = Duration.ofMilliseconds(1000L),
    minDistance = Distance.ofMeters(10.0)
)

private val pinImage = ImageFactory.fromAssets("icons/location_96.png")

@Composable
fun TomMap(
    navController: NavHostController,
    paddingValues: PaddingValues,
    context: Context = LocalContext.current,
    markers: StateFlow<List<MapMarker>>,
    setMarkerHandler: (mapMarker: MapMarker) -> Unit = { _ -> },
    saveMarkerHandler: (List<MapMarker>) -> Unit
) {
    val mapMarkers = mutableListOf<MapMarker>().apply {
        addAll(markers.collectAsState().value)
    }
    val showMarkerDialog = remember { mutableStateOf(false) }
    val balloonText = remember { mutableStateOf("") }



    if (showMarkerDialog.value) {
        MarkerDialog(
            balloonText = balloonText.value,
            onDismissAction = { showMarkerDialog.value = false }
        )
    }


    val locationEngine: LocationEngine = AndroidLocationEngine(
        context = context,
        config = androidLocationEngineConfig
    )

    val cameraOptions = rememberSaveable {
        mutableListOf(
            CameraOptions(
                position = getCurrentLocation(locationEngine),
                zoom = 15.0,
                tilt = 0.0
            )
        )
    }

    val mapOptions = remember {
        MapOptions(
            mapKey = BuildConfig.MAPS_API_KEY,
            cameraOptions = cameraOptions.first()
        )
    }

    val mapView = MapView(context = context, mapOptions = mapOptions)
    mapView.onCreate(bundle = null)
    mapView.getMapAsync { map ->
        map.setLocationEngine(locationEngine)
        map.enableLocationMarker(LocationMarkerOptions(LocationMarkerType.CHEVRON))
        locationEngine.enable()

        map.isMarkersFadingEnabled = false
        map.isMarkersShrinkingEnabled = false

        map.addOnMapLongClickListener { markerCoordinate ->
            val mapMarker = MapMarker(
                title = "Marker",
                text = "Marker: " +
                        "${Converter.latToLatDeg(markerCoordinate.latitude)}, " +
                        Converter.longToLongDeg(markerCoordinate.longitude),
                lat = markerCoordinate.latitude,
                long = markerCoordinate.longitude
            )

            mapMarkers.add(mapMarker)
            saveMarkerHandler(mapMarkers)

            map.addMarker(
                MarkerOptions(
                    coordinate = markerCoordinate,
                    balloonText = mapMarker.text,
                    pinImage = pinImage
                )
            )

            setMarkerHandler(mapMarker)
            navController.navigate(Screens.Marker.route)
            true
        }

        map.addOnMarkerClickListener { marker: Marker ->
            if (!marker.isSelected()) {
                balloonText.value = marker.balloonText
                showMarkerDialog.value = true
            }
        }

        map.addOnCameraChangeListener {
            cameraOptions.add(
                0, CameraOptions(
                    position = map.cameraPosition().position,
                    zoom = map.cameraPosition().zoom,
                    tilt = map.cameraPosition().tilt
                )
            )
        }
    }

    AndroidView(
        factory = {
            mapView
        },
        modifier = Modifier.padding(paddingValues = paddingValues)
    ) {
        it.onResume()
        it.getMapAsync { map ->
            mapMarkers.forEach { mark ->
                map.addMarker(
                    MarkerOptions(
                        coordinate = GeoCoordinate(latitude = mark.lat, longitude = mark.long),
                        balloonText = mark.text,
                        pinImage = pinImage
                    )
                )
            }
        }
    }
}

private fun getCurrentLocation(androidLocationEngine: LocationEngine): GeoCoordinate {
    androidLocationEngine.enable()
    val coordinates = androidLocationEngine
        .lastKnownLocation?.position ?: GeoCoordinate(55.0, 83.0)
    androidLocationEngine.disable()
    return coordinates
}