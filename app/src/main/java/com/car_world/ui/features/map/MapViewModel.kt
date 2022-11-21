package com.car_world.ui.features.map

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.car_world.R
import com.car_world.core.map.constants.MapConstants
import com.car_world.core.map.entities.TileCoordinates
import com.car_world.core.utils.normalize
import com.car_world.infrastructure.cars.interfaces.ICarsRepo
import com.car_world.infrastructure.map.interfaces.IMapRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ovh.plrapps.mapcompose.api.*
import ovh.plrapps.mapcompose.core.TileStreamProvider
import ovh.plrapps.mapcompose.ui.state.MapState
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val mapRepo: IMapRepo,
    private val carsRepo: ICarsRepo,
) : ViewModel() {

    // Provider used to get tiles for the map.
    private val tileStreamProvider = makeTileStreamProvider()

    init {
        // Get cars and create new markers.
        viewModelScope.launch {
            val carsList = carsRepo.getCars(true)

            for ( car in carsList ) {

                val locationX = normalize(car.longitude, -180.0, 180.0)
                // Due to the Mercator latitude deformity, I slightly changed the max value for the
                // normalization in order for the data to be closer to its real location.
                val locationY = 1.0 - normalize(car.latitude, 0.0, 73.7192)
                addMarker(car.id, locationX, locationY, car.licensePlate)
            }

            if (carsList.isNotEmpty()) {

                onCenter(carsList[0].id, 2f)
            }
        }
    }

    private fun makeTileStreamProvider(): TileStreamProvider {

        return TileStreamProvider { row, col, level ->
            mapRepo.getDataTile(TileCoordinates(level, col, row))
        }
    }

    val state: MapState by mutableStateOf(
        MapState(
            MapConstants.MAX_ZOOM_LEVEL,
            MapConstants.MERCATOR_MAPSIZE_PIXELS,
            MapConstants.MERCATOR_MAPSIZE_PIXELS,
            workerCount = MapConstants.NUMBER_WORKERS
        ) {
            scale(MapConstants.SCALE)
            maxScale(MapConstants.MAX_SCALE)
            scroll(0.5, 0.5)

        }.apply {
            addLayer(tileStreamProvider)
            disableFlingZoom()
            setMagnifyingFactor(1)
            enableRotation()
        }
    )

    /**
     * Add a marker to the map.
     *
     * @param id The id of the marker.
     * @param x The x coordinate of the marker normalized 0..1.
     * @param y The y coordinate of the marker normalized 0..1.
     * @param text The text to display on the marker.
     */
    fun addMarker(id: String, x: Double, y: Double, text: String = "") {
        state.setScrollOffsetRatio(0f, 0f)
        state.addMarker(id, x, y, clipShape = RectangleShape ) {
            Text(
                text = text,
                color = Color.Black,
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_car_foreground),
                contentDescription = null,
                modifier = Modifier.size(50.dp),
                tint = Color(0xCC2196F3)
            )
        }
    }

    /**
     * Center the map on a marker specified.
     *
     * @param markId The id of the marker to center on.
     * @param destScale The scale to zoom the marker to.
     */
    fun onCenter(markId: String, destScale: Float) {
        viewModelScope.launch {
            state.centerOnMarker(markId, destScale)
        }
    }

    /**
     * Rotate the map.
     *
     * @param angle The angle to rotate the map to.
     * @param increment If true, the angle will be added to the current rotation.
     */
    fun onRotate(angle: Float, increment: Boolean = false) {
        viewModelScope.launch {
            if (increment) {
                state.rotateTo(state.rotation + 90f)
            }
            else {
                state.rotateTo(angle)
            }
        }
    }
}