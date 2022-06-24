package com.eugenics.mapapplication.ui.screens.map

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.FragmentManager
import com.eugenics.mapapplication.BuildConfig
import com.eugenics.mapapplication.databinding.MapFrameViewBinding
import com.tomtom.sdk.common.measures.Distance
import com.tomtom.sdk.common.time.Duration
import com.tomtom.sdk.location.LocationEngine
import com.tomtom.sdk.location.android.AndroidLocationEngine
import com.tomtom.sdk.location.android.AndroidLocationEngineConfig
import com.tomtom.sdk.maps.display.MapOptions
import com.tomtom.sdk.maps.display.camera.CameraOptions
import com.tomtom.sdk.maps.display.location.LocationMarkerOptions
import com.tomtom.sdk.maps.display.location.LocationMarkerType
import com.tomtom.sdk.maps.display.ui.MapFragment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    context: Context,
    fragmentManager: FragmentManager
) {
    Scaffold { paddingValues ->
        Map(
            paddingValues = paddingValues,
            supportFragmentManager = fragmentManager,
            context = context
        )
    }
}

@Composable
private fun Map(
    paddingValues: PaddingValues,
    context: Context = LocalContext.current,
    supportFragmentManager: FragmentManager
) {
    val androidLocationEngineConfig = AndroidLocationEngineConfig(
        minTimeInterval = Duration.ofMilliseconds(250L),
        minDistance = Distance.ofMeters(20.0)
    )
    val locationEngine: LocationEngine = AndroidLocationEngine(
        context = context,
        config = androidLocationEngineConfig
    ).also {
        it.enable()
    }

    val cameraOptions = CameraOptions(
        position = locationEngine.lastKnownLocation?.position,
        zoom = 16.0,
        tilt = 0.0
    )
    val mapOptions = MapOptions(
        mapKey = BuildConfig.MAPS_API_KEY,
        cameraOptions = cameraOptions
    )
    val mapFragment = MapFragment.newInstance(mapOptions = mapOptions)

    AndroidViewBinding(
        factory = MapFrameViewBinding::inflate,
        modifier = Modifier.padding(paddingValues = paddingValues),
    ) {
        mapFragment.getMapAsync { map ->
            locationEngine.disable()
            map.setLocationEngine(locationEngine)
            map.enableLocationMarker(LocationMarkerOptions(LocationMarkerType.CHEVRON))
            locationEngine.enable()
        }
        supportFragmentManager.beginTransaction()
            .replace(this.mapContainer.id, mapFragment)
            .commit()
    }
}