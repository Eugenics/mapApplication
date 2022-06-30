package com.eugenics.mapapplication.data.entities

import com.eugenics.mapapplication.domain.model.MapMarker

data class MarkerEntity(
    val title: String,
    val text: String = "",
    val lat: Double,
    val long: Double
)

fun MarkerEntity.mapToModel(): MapMarker =
    MapMarker(
        title = this.title,
        text = this.text,
        lat = this.lat,
        long = this.long
    )