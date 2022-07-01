package com.eugenics.mapapplication.domain.model

import com.eugenics.mapapplication.data.entities.MarkerEntity
import com.eugenics.mapapplication.domain.util.Converter

data class MapMarker(
    val title: String = "",
    val text: String = "",
    val lat: Double = 0.0,
    val long: Double = 0.0,
    val latDeg: String = Converter.latToLatDeg(lat),
    val longDeg: String = Converter.longToLongDeg(long)
)

fun MapMarker.mapToEntity(): MarkerEntity =
    MarkerEntity(
        title = this.title,
        text = this.text,
        lat = this.lat,
        long = this.long
    )