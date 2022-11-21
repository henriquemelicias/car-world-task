package com.car_world.infrastructure.map

import com.car_world.core.map.entities.TileCoordinates
import com.car_world.infrastructure.map.interfaces.IMapDataSource
import com.car_world.infrastructure.map.interfaces.IMapRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream

/**
 * MapRepo is a repository for map data.
 */
class MapRepo(
    private val mapRemoteDataSource: IMapDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : IMapRepo {

    // Mutex to make writes to cached values thread-safe.
    private val dataTileMapMutex = Mutex()

    // Cache of the map tiles.
    private var dataTileMap: Map<TileCoordinates, BufferedInputStream> = emptyMap()

    override suspend fun getDataTile(tileCoordinates: TileCoordinates): BufferedInputStream? {

        return if (this.dataTileMap.containsKey(tileCoordinates)) {
            return dataTileMapMutex.withLock { this.dataTileMap[tileCoordinates] }
        }
        else {
            withContext(ioDispatcher) {
                mapRemoteDataSource.getTileData(tileCoordinates)
            }
        }
    }
}