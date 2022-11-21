package com.car_world.infrastructure.map.remote

import com.car_world.core.map.entities.TileCoordinates
import com.car_world.infrastructure.map.interfaces.IMapDataSource
import com.car_world.infrastructure.map.interfaces.IMapDataSourceApi
import java.io.BufferedInputStream

class MapRemoteDataSource (
    private val mapApi: IMapDataSourceApi,
): IMapDataSource {

    override suspend fun getTileData(tileCoordinates: TileCoordinates): BufferedInputStream? {
        return mapApi.fetchDataTileFromWebsite(tileCoordinates.zoomLevel, tileCoordinates.col, tileCoordinates.row)
    }
}