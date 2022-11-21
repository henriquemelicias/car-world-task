package com.car_world.infrastructure.cars.local

import android.content.Context
import com.car_world.core.cars.entities.CarModel
import com.car_world.core.utils.ErrorMessage
import com.car_world.core.utils.Failure
import com.car_world.core.utils.Result
import com.car_world.core.utils.Success
import com.car_world.infrastructure.cars.interfaces.ICarsDataSource
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Local data source for cars
 */
class CarsLocalDataSource(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher
): ICarsDataSource {

    @OptIn(ExperimentalStdlibApi::class)
    override suspend fun getCars(): Result<List<CarModel>, ErrorMessage> = withContext(ioDispatcher) {
        val carsJson = context
            .assets
            .open("cars.json")
            .bufferedReader()
            .use { it.readText() }

        val moshi: Moshi = Moshi.Builder().build()
        val carsListJsonAdapter: JsonAdapter<List<CarModel>> = moshi.adapter()

        val maybeCarsList = carsListJsonAdapter.fromJson(carsJson)

        maybeCarsList?.let {
            Success(maybeCarsList)
        } ?: run {
            Failure(ErrorMessage("Error: Failed to convert cars in json to a list"))
        }
    }
}