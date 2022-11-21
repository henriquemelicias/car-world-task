package com.car_world.infrastructure.cars.interfaces

import com.car_world.core.cars.entities.CarModel
import com.car_world.core.utils.ErrorMessage
import com.car_world.core.utils.Result
import com.car_world.infrastructure.cars.remote.CarsRemoteDataSourceApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

/**
 * Interface for the data source that will be responsible for getting the cars from the API
 */
interface ICarsDataSourceApi {
    /**
     * Get the list of cars from the API
     *
     * @param url The url to get the cars from
     *
     * @return A [Result] with the list of [CarModel]s or an [ErrorMessage]
     */
    fun fetchCarsFromJson(url: String): Result<List<CarModel>, ErrorMessage>
}
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RemoteCarsDataSourceApi

@Module
@InstallIn(SingletonComponent::class)
object RemoteCarsDataSourceApiModule {

    @Provides
    @RemoteCarsDataSourceApi
    fun provideRemoteCarsDataSourceApi(): ICarsDataSourceApi = CarsRemoteDataSourceApi
}
