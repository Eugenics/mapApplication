package com.eugenics.mapapplication.ui.screens.map.components

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.eugenics.mapapplication.BuildConfig
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

private val androidLocationEngineConfig = AndroidLocationEngineConfig(
    minTimeInterval = Duration.ofMilliseconds(1000L),
    minDistance = Distance.ofMeters(10.0)
)

@Composable
fun TomMap(
    paddingValues: PaddingValues,
    context: Context = LocalContext.current
) {
    val markers = rememberSaveable { mutableListOf<MarkerOptions>() }
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

        if (markers.isNotEmpty()) {
            markers.forEach {
                map.addMarker(it)
            }
        }

        map.addOnMapLongClickListener { coordinate ->
            val markerOptions = MarkerOptions(
                coordinate = coordinate,
                balloonText = "Marker: ${coordinate.longitude},${coordinate.latitude}",
                pinImage = ImageFactory.fromAssets("icons/location_96.png")
            )
            markers.add(markerOptions)
            map.addMarker(markerOptions)
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
    }
}

private fun getCurrentLocation(androidLocationEngine: LocationEngine): GeoCoordinate {
    androidLocationEngine.enable()
    val coordinates = androidLocationEngine
        .lastKnownLocation?.position ?: GeoCoordinate(55.0, 83.0)
    androidLocationEngine.disable()
    return coordinates
}