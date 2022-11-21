package com.car_world.core.utils

fun normalize(t: Double, min: Double, max: Double): Double {
    return (t - min) / (max - min)
}

fun deNormalize(t: Double, min: Double, max: Double): Double {
    return min + t * (max - min)
}
