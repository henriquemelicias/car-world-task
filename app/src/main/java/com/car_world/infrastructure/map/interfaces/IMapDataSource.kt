package com.car_world.infrastructure.map.interfaces

import com.car_world.core.map.entities.TileCoordinates
import com.car_world.infrastructure.map.remote.MapRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.io.BufferedInputStream
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Map data source interface.
 */
interface IMapDataSource {
    /**
     * Get map tile from the data source.
     *
     * @param tileCoordinates tile coordinates
     *
     * @return map tile in a [BufferedInputStream]
     */
    suspend fun getTileData(tileCoordinates: TileCoordinates): BufferedInputStream?
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RemoteMapDataSource

@Module
@InstallIn(SingletonComponent::class)
object MapDataSourceModule {

    @Singleton
    @RemoteMapDataSource
    @Provides
    fun provideMapRemoteDataSource(
        @RemoteMapDataSourceApi mapRemoteDataSourceApi: IMapDataSourceApi,
    ) : IMapDataSource {
        return MapRemoteDataSource( mapRemoteDataSourceApi )
    }
}
