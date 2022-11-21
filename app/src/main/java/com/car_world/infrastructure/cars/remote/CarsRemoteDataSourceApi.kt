package com.car_world.infrastructure.cars.remote

import com.car_world.core.cars.entities.CarModel
import com.car_world.core.utils.ErrorMessage
import com.car_world.core.utils.Failure
import com.car_world.core.utils.Result
import com.car_world.core.utils.Success
import com.car_world.infrastructure.cars.interfaces.ICarsDataSourceApi
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

object CarsRemoteDataSourceApi: ICarsDataSourceApi {

    @OptIn(ExperimentalStdlibApi::class)
    override fun fetchCarsFromJson(url: String): Result<List<CarModel>, ErrorMessage> {

        return try {
            val connection = URL(url).openConnection() as HttpURLConnection
            val data = connection.inputStream.bufferedReader().readText()

            val moshi: Moshi = Moshi.Builder().build()
            val carsListJsonAdapter: JsonAdapter<List<CarModel>> = moshi.adapter()

            return carsListJsonAdapter.fromJson(data)?.let {
                Success(it)
            } ?: run {
                Failure(ErrorMessage("Error deserializing cars list json from API"))
            }

        } catch (e: IOException) {
            Failure(ErrorMessage(e.message))
        }
    }
}