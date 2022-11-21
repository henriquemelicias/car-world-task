package com.car_world.infrastructure.map.remote

import com.car_world.infrastructure.map.interfaces.IMapDataSourceApi
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream

object MapRemoteDataSourceApi: IMapDataSourceApi {

    private val httpClient = HttpClient(CIO)

    override suspend fun fetchDataTileFromWebsite(zoom: Int, col: Int, row: Int): BufferedInputStream? {

        val url = "https://tile.openstreetmap.org/$zoom/$col/$row.png"

        var result: BufferedInputStream? = null
        try {
            val response: HttpResponse = httpClient.get(url)
            val byteArrayBody: ByteArray = response.body()
            result = BufferedInputStream(ByteArrayInputStream(byteArrayBody))
        } catch (_: Exception) {
        }

        return result
    }

}