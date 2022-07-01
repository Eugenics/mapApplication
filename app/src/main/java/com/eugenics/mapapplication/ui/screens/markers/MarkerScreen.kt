package com.eugenics.mapapplication.ui.screens.markers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.eugenics.mapapplication.R
import com.eugenics.mapapplication.domain.model.MapMarker
import com.eugenics.mapapplication.domain.util.Converter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkerScreen(
    navController: NavHostController,
    marker: MapMarker,
    saveMarkerHandler: (MapMarker) -> Unit = { _ -> }
) {
    val text = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.marker),
                        style = MaterialTheme.typography.titleSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues = paddingValues)
        ) {
            OutlinedTextField(
                value = text.value,
                onValueChange = { text.value = it },
                label = { Text(text = stringResource(R.string.title)) },
                modifier = Modifier.fillMaxWidth()
                    .padding(10.dp)
            )

            Button(
                onClick = {
                    saveMarkerHandler(
                        MapMarker(
                            title = text.value,
                            text = text.value +
                                    " ${Converter.latToLatDeg(marker.lat)}, " +
                                    Converter.longToLongDeg(marker.long),
                            lat = marker.lat,
                            long = marker.long
                        )
                    )
                    navController.navigateUp()
                },
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    text = stringResource(R.string.save),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}