# Car world: Simple android app task

A simple android app that fetches and showcases cars on a list and on the map.

Internet is required for the map data tiles.

## External libraries/frameworks used:

* Jetpack Compose:
Android's recommended modern toolkit for native UI development.

* HILT/Dagger:
As a dependency injection library to reduce boilerplate of doing it manually.

* moshi (json):
A Serialization/Deserialization library for JSON.

* Mockserver:
Java's mock server for testing unit cases which require variable data that we can't ensure it will stay the same. 
Thus, we create a mock server with mock data.

* Ktor:
Framework for async clinet/server connections. 
I was not going to use this library initially, but the site in which I obtain the map data tiles was
refusing to work when using java.net.HttpClient.

* MapCompose:
https://github.com/p-lr/MapCompose

It's a library to display tiled maps with Jetpack Compose.
I use OpenStreetMaps as the tiles source.


## Atention:
The map used is a Mercator Map Projection, the latitude might not be as accurate as expected.

I did not have time to apropriate this app to multiple layouts. A smartphone on compact was the layout used during development.
