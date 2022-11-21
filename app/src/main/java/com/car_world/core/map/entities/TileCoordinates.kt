package com.car_world.core.map.entities

/**
 * Represents a tile's coordinates on the map. The origin is at the top left corner of the map.
 * For each zoom level, the number of tiles is doubled.
 *
 * @param zoomLevel zoom level.
 * @param col column index
 * @param row row index
 */
data class TileCoordinates(val zoomLevel: Int, val col: Int, val row: Int)
