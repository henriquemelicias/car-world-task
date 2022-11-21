package com.car_world.infrastructure.map.interfaces

import com.car_world.infrastructure.map.remote.MapRemoteDataSourceApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.io.BufferedInputStream
import javax.inject.Qualifier

/**
 * Map data source interface.
 */
interface IMapDataSourceApi {
    /**
     * Fetch map tile from remote data source.
     *
     * @param zoom zoom level
     * @param col column
     * @param row row
     *
     * @return [BufferedInputStream] of the tile data
     */
    suspend fun fetchDataTileFromWebsite(zoom: Int, col: Int, row: Int): BufferedInputStream?
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RemoteMapDataSourceApi

@Module
@InstallIn(SingletonComponent::class)
object RemoteMapDataSourceApiModule {

    @Provides
    @RemoteMapDataSourceApi
    fun provideRemoteMapDataSourceApi(): IMapDataSourceApi = MapRemoteDataSourceApi
}
