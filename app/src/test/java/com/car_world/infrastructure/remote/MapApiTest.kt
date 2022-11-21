package com.car_world.infrastructure.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.ByteArrayInputStream

class MapApiTest {


    @Test
    fun fetchMapTile__simpleValues__returnsTile() = runBlocking {

        val level = 2
        val row = 2
        val col = 2

        val url = "https://tile.openstreetmap.org/$level/$row/$col.png"
        val client = HttpClient(CIO)
        val response: HttpResponse = client.get(url)
        val byteArrayBody: ByteArray = response.body()
        val inputStream = ByteArrayInputStream( byteArrayBody )

        assert(true)
    }
}