package com.car_world.infrastructure.cars

import com.car_world.core.cars.entities.CarModel
import com.car_world.infrastructure.cars.interfaces.ICarsDataSource
import com.car_world.infrastructure.cars.interfaces.ICarsRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/**
 * CarsRepo is a repository that provides access to the cars data.
 */
class CarsRepo(
    private val carsLocalDataSource: ICarsDataSource,
    private val carsRemoteDataSource: ICarsDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : ICarsRepo {

    // Mutex to make writes to cached values thread-safe.
    private val carsListMutex = Mutex()

    // Cache of carsList.
    private var carsList: List<CarModel> = emptyList()

    override suspend fun getCars(refresh: Boolean): List<CarModel> {

        return if (refresh) {

            withContext(ioDispatcher) {

                carsRemoteDataSource
                    .getCars()
                    .orElse(
                        // This should never fail, getting from local data source is a fallback.
                        carsLocalDataSource.getCars().expect("Error: failed to get local cars list")
                    ).also { result ->

                        // Thread-safe write.
                        carsListMutex.withLock {
                            carsList = result
                        }
                    }
            }

        } else {
            return carsListMutex.withLock { this.carsList }
        }
    }
}