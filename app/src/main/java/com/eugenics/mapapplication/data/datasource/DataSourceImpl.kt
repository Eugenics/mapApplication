package com.eugenics.mapapplication.data.datasource

import android.util.Log
import com.eugenics.mapapplication.data.entities.MarkerEntity
import com.eugenics.mapapplication.domain.interfaces.DataSource
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

class DataSourceImpl : DataSource {
    override fun writeTo(
        output: OutputStream,
        markers: List<MarkerEntity>
    ) {
        try {
            output.write(Json.encodeToString(listSerializer, markers).toByteArray())
        } catch (ex: Exception) {
            Log.e(TAG, ex.message.toString())
        }
    }

    override fun readFrom(input: InputStream): List<MarkerEntity> {
        val defaultValue: List<MarkerEntity> = listOf()
        return try {
            Json.decodeFromString(listSerializer, input.readBytes().decodeToString())
        } catch (ex: SerializationException) {
            Log.e(TAG, ex.message.toString())
            defaultValue
        }
    }

    companion object {
        val TAG = "DATA_SOURCE"
        private val listSerializer = ListSerializer(MarkerEntity.serializer())
    }
}