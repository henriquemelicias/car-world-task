package com.car_world.infrastructure.map.interfaces

import com.car_world.core.map.entities.TileCoordinates
import com.car_world.di.IoDispatcher
import com.car_world.infrastructure.map.MapRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import java.io.BufferedInputStream
import javax.inject.Singleton

interface IMapRepo {
    /**
     * Returns a buffered input stream of the tile image
     *
     * @param tileCoordinates the coordinates of the tile
     *
     * @return a buffered input stream of the tile
     */
    suspend fun getDataTile(tileCoordinates: TileCoordinates): BufferedInputStream?

@Module
@InstallIn(SingletonComponent::class)
object MapRepoModule {

    @Singleton
    @Provides
    fun provideMapRepo(
        @RemoteMapDataSource mapRemoteDataSource: IMapDataSource,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): IMapRepo {
        return MapRepo( mapRemoteDataSource, ioDispatcher )
    }
}
}