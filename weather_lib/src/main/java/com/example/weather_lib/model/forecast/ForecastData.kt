package com.example.weather_lib.model.forecast

data class ForecastData(
    val city: City,
    val cnt: Int, // 8
    val cod: String, // 200
    val list: List<ForecastWeatherData>,
    val message: Int // 0
)