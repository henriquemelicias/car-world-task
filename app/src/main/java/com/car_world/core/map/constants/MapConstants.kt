package com.car_world.core.map.constants

object MapConstants {
    // Maximum zoom level of the pyramid tiled map.
    const val MAX_ZOOM_LEVEL = 19
    // The size of the mercator tile maps in pixels.
    const val MERCATOR_MAPSIZE_PIXELS = 67108864
    // Number of workers use by the map (recommended 16 or higher when using tile from HTTP source).
    const val NUMBER_WORKERS = 32
    // Map scale.
    const val SCALE = 1f
    // Map max scale (on zoom).
    const val MAX_SCALE = 5f
}