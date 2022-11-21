package com.car_world.infrastructure.cars.interfaces

import com.car_world.core.cars.entities.CarModel
import com.car_world.di.IoDispatcher
import com.car_world.infrastructure.cars.CarsRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

/**
 * Interface for the [CarsRepo] class.
 */
interface ICarsRepo {
    /**
     * Gets a list of [CarModel]s from the API.
     *
     * @param refresh Whether to refresh the data from the API or not.
     *
     * @return A list of [CarModel]s.
     */
    suspend fun getCars(refresh: Boolean = false): List<CarModel>
}

@Module
@InstallIn(SingletonComponent::class)
object CarsRepoModule {

    @Singleton
    @Provides
    fun provideCarsRepo(
        @LocalCarsDataSource carsLocalDataSource: ICarsDataSource,
        @RemoteCarsDataSource carsRemoteDataSource: ICarsDataSource,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): ICarsRepo {
        return CarsRepo( carsLocalDataSource, carsRemoteDataSource, ioDispatcher )
    }
}
