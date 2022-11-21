package com.car_world.infrastructure.cars.remote

import com.car_world.core.cars.entities.CarModel
import com.car_world.core.utils.ErrorMessage
import com.car_world.core.utils.Result
import com.car_world.infrastructure.cars.interfaces.ICarsDataSource
import com.car_world.infrastructure.cars.interfaces.ICarsDataSourceApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Remote data source for cars
 */
class CarsRemoteDataSource (
    private val carsApi: ICarsDataSourceApi,
    private val ioDispatcher: CoroutineDispatcher
): ICarsDataSource {

    override suspend fun getCars(): Result<List<CarModel>, ErrorMessage> = withContext(ioDispatcher) {
        carsApi.fetchCarsFromJson("https://cdn.sixt.io/codingtask/cars")
    }
}