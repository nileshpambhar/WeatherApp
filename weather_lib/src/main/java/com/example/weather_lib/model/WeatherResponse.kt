package com.example.weather_lib.model

data class WeatherResponse(
    val base: String, // stations
    val clouds: Clouds,
    val cod: Int, // 200
    val coord: Coord,
    val dt: Int, // 1661404611
    val id: Int, // 1279233
    val main: Main,
    val name: String, // Ahmedabad
    val sys: Sys,
    val timezone: Int, // 19800
    val visibility: Int, // 4000
    val weather: List<Weather>,
    val wind: Wind
)