package com.eugenics.mapapplication.ui.screens.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eugenics.mapapplication.data.datasource.DataSourceFactory
import com.eugenics.mapapplication.data.entities.MarkerEntity
import com.eugenics.mapapplication.data.entities.mapToModel
import com.eugenics.mapapplication.domain.interfaces.DataSource
import com.eugenics.mapapplication.domain.model.MapMarker
import com.eugenics.mapapplication.domain.model.mapToEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class MapViewModel(
    private val dataSource: DataSource = DataSourceFactory.build()
) : ViewModel() {

    private val _markers = MutableStateFlow(listOf<MapMarker>())
    val markers: StateFlow<List<MapMarker>> = _markers

    private val _marker = MutableStateFlow(MapMarker())
    val marker: StateFlow<MapMarker> = _marker

    private var path: String? = null

    init {
        readMarkers()
    }

    fun writeMarkers(markers: List<MapMarker>) {
        viewModelScope.launch(Dispatchers.IO) {
            path?.let {
                val markersEntities: List<MarkerEntity> = markers.map { it.mapToEntity() }
                val file = File(it)
                file.parent?.let { root ->
                    if (Files.notExists(Paths.get(root))) {
                        Files.createDirectory(Paths.get(root))
                    }
                }
                dataSource.writeTo(file.outputStream(), markersEntities)
                readMarkers()
            }
        }
    }

    private fun readMarkers() {
        viewModelScope.launch(Dispatchers.IO) {
            path?.let {
                val file = File(it)
                if (file.exists()) {
                    _markers.value = dataSource.readFrom(file.inputStream()).map { entity ->
                        entity.mapToModel()
                    }
                }
            }
        }
    }

    fun setMarker(mapMarker: MapMarker) {
        _marker.value = mapMarker
    }

    fun saveMarker(mapMarker: MapMarker) {
        val savedMarkers = mutableListOf<MapMarker>().apply {
            this.addAll(markers.value)
            this.remove(marker.value)
            this.add(mapMarker)
        }
        writeMarkers(markers = savedMarkers)
    }

    private fun setPath(path: String?) {
        this.path = path
    }

    companion object {
        const val TAG = "MAP_VIEW_MODEL"
        fun newInstance(dataFilePath: String): MapViewModel {
            val viewModel = MapViewModel()
            viewModel.setPath(dataFilePath)
            return viewModel
        }
    }
}

