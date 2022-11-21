package com.car_world.infrastructure.cars.interfaces

import android.content.Context
import com.car_world.core.cars.entities.CarModel
import com.car_world.core.utils.ErrorMessage
import com.car_world.core.utils.Result
import com.car_world.di.IoDispatcher
import com.car_world.infrastructure.cars.local.CarsLocalDataSource
import com.car_world.infrastructure.cars.remote.CarsRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Interface for the data source that is responsible for getting the cars data.
 */
interface ICarsDataSource {

    /**
     * Gets the cars data from the data source.
     *
     * @return [Result] of [List] of [CarModel] or [ErrorMessage] if an error occurred.
     */
    suspend fun getCars(): Result<List<CarModel>, ErrorMessage>
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LocalCarsDataSource

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RemoteCarsDataSource

@Module
@InstallIn(SingletonComponent::class)
object CarsDataSourceModule {

    @Singleton
    @LocalCarsDataSource
    @Provides
    fun provideCarsLocalDataSource(@ApplicationContext context: Context, @IoDispatcher ioDispatcher: CoroutineDispatcher): ICarsDataSource {
        return CarsLocalDataSource( context, ioDispatcher )
    }

    @Singleton
    @RemoteCarsDataSource
    @Provides
    fun provideCarsRemoteDataSource(
        @RemoteCarsDataSourceApi carsRemoteDataSourceApi: ICarsDataSourceApi,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ) : ICarsDataSource {
        return CarsRemoteDataSource( carsRemoteDataSourceApi, ioDispatcher )
    }
}
