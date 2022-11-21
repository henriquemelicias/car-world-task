package com.car_world.core.cars.entities

import com.squareup.moshi.JsonClass

// TODO: Change fuelType?, transmission?, cleanliness into enum classes ?

/**
 * Car model entity.
 */
@JsonClass(generateAdapter = true)
data class CarModel(
    val id: String,
    val modelIdentifier: String,
    val modelName: String,
    val name: String,
    val make: String,
    val group: String,
    val color: String,
    val series: String,
    val fuelType: Char,
    val fuelLevel: Float,
    val transmission: Char,
    val licensePlate: String,
    val latitude: Double,
    val longitude: Double,
    val innerCleanliness: String,
    val carImageUrl: String,
)