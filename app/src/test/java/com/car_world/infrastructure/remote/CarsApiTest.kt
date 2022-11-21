package com.car_world.infrastructure.remote

import com.car_world.core.cars.entities.CarModel
import com.car_world.core.utils.ErrorMessage
import com.car_world.core.utils.Failure
import com.car_world.core.utils.Success
import com.car_world.infrastructure.cars.remote.CarsRemoteDataSourceApi
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okhttp3.HttpUrl
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 */
class CarsApiTest {
    @Test
    fun fetchCars_responseWithoutEntries_returnsEmptyCarsList() {
        val responseJson = "[]"
        val expectedResult = Success<List<CarModel>, ErrorMessage>(
            listOf()
        )

        val server = MockWebServer()
        val response: MockResponse = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setBody(responseJson)
        server.enqueue(response)
        server.start()

        val url: HttpUrl = server.url("/get")

        val actualResult = CarsRemoteDataSourceApi.fetchCarsFromJson(url.toString())

        server.shutdown()

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun fetchCars_responseWithMultipleEntries_returnsCarsList() {
        val responseJson = "[{\"id\": \"WMWSW31030T222518\",\"modelIdentifier\": \"mini\",\"modelName\": \"MINI\",\"name\": \"Vanessa\",\"make\": \"BMW\",\"group\": \"MINI\",\"color\": \"midnight_black\",\"series\": \"MINI\",\"fuelType\": \"D\",\"fuelLevel\": 0.7,\"transmission\": \"M\",\"licensePlate\": \"M-VO0259\",\"latitude\": 48.134557,\"longitude\": 11.576921,\"innerCleanliness\": \"REGULAR\",\"carImageUrl\": \"https://cdn.sixt.io/codingtask/images/mini.png\" },{\"id\": \"WMWSW31030T222518\",\"modelIdentifier\": \"mini\",\"modelName\": \"MINI\",\"name\": \"Vanessa\",\"make\": \"BMW\",\"group\": \"MINI\",\"color\": \"midnight_black\",\"series\": \"MINI\",\"fuelType\": \"D\",\"fuelLevel\": 0.7,\"transmission\": \"M\",\"licensePlate\": \"M-VO0259\",\"latitude\": 48.134557,\"longitude\": 11.576921,\"innerCleanliness\": \"REGULAR\",\"carImageUrl\": \"https://cdn.sixt.io/codingtask/images/mini.png\" }]"
        val expectedResult = Success<List<CarModel>, ErrorMessage>(
            listOf(
                CarModel(
                    id="WMWSW31030T222518",
                    modelIdentifier="mini",
                    modelName="MINI",
                    name="Vanessa",
                    make="BMW",
                    group="MINI",
                    color="midnight_black",
                    series="MINI",
                    fuelType='D',
                    fuelLevel=0.7F,
                    transmission='M',
                    licensePlate="M-VO0259",
                    latitude=48.134557,
                    longitude=11.576921,
                    innerCleanliness="REGULAR",
                    carImageUrl="https://cdn.sixt.io/codingtask/images/mini.png"
                ),
                CarModel(
                    id="WMWSW31030T222518",
                    modelIdentifier="mini",
                    modelName="MINI",
                    name="Vanessa",
                    make="BMW",
                    group="MINI",
                    color="midnight_black",
                    series="MINI",
                    fuelType='D',
                    fuelLevel=0.7F,
                    transmission='M',
                    licensePlate="M-VO0259",
                    latitude=48.134557,
                    longitude=11.576921,
                    innerCleanliness="REGULAR",
                    carImageUrl="https://cdn.sixt.io/codingtask/images/mini.png"
                )
            )
        )

        val server = MockWebServer()
        val response: MockResponse = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setBody(responseJson)
        server.enqueue(response)
        server.start()

        val url: HttpUrl = server.url("/get")

        val actualResult = CarsRemoteDataSourceApi.fetchCarsFromJson(url.toString())

        server.shutdown()

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun fetchCars_invalidResponse_returnsFailResult() {
        val responseJson = "Invalid Json Oh no!!!"
        val expectedResult = Failure<List<CarModel>, ErrorMessage>(ErrorMessage("Use JsonReader.setLenient(true) to accept malformed JSON at path $"))

        val server = MockWebServer()
        val response: MockResponse = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setBody(responseJson)
        server.enqueue(response)
        server.start()

        val url: HttpUrl = server.url("/get")

        val actualResult = CarsRemoteDataSourceApi.fetchCarsFromJson(url.toString())

        server.shutdown()

        assertEquals(expectedResult, actualResult)
    }


    @Test
    fun fetchCars_invalidUrl_returnsFailResult() {
        val expectedResult = Failure<List<CarModel>, ErrorMessage>(ErrorMessage("no protocol: invalid-url"))

        val actualResult = CarsRemoteDataSourceApi.fetchCarsFromJson("invalid-url")

        assertEquals(expectedResult, actualResult)
    }
}