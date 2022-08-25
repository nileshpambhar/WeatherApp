package com.example.weather_lib.model.forecast

import com.example.weather_lib.model.Coord

data class City(
    val coord: Coord,
    val country: String, // RU
    val id: Int, // 524901
    val name: String, // Moscow
    val population: Int, // 0
    val sunrise: Int, // 1661394028
    val sunset: Int, // 1661445803
    val timezone: Int // 10800
)