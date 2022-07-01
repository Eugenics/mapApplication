package com.eugenics.mapapplication.domain.interfaces

import com.eugenics.mapapplication.data.entities.MarkerEntity
import java.io.InputStream
import java.io.OutputStream

interface DataSource {
    fun writeTo(output: OutputStream, markers: List<MarkerEntity>)
    fun readFrom(input: InputStream): List<MarkerEntity>
}