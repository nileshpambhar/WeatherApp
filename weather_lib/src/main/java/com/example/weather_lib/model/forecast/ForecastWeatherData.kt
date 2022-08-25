package com.example.weather_lib.model.forecast

import com.example.weather_lib.model.*

data class ForecastWeatherData(
    val clouds: Clouds,
    val dt: Int, // 1661418000
    val dt_txt: String, // 2022-08-25 09:00:00
    val main: Main,
    val pop: Int, // 0
    val sys: Sys,
    val visibility: Int, // 10000
    val weather: List<Weather>,
    val wind: Wind
)