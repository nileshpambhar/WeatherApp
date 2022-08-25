package com.example.weather_lib.network

import retrofit2.http.GET
import com.example.weather_lib.model.WeatherResponse
import com.example.weather_lib.model.forecast.ForecastData
import retrofit2.Call
import retrofit2.http.QueryMap

interface WeatherApiService {

    companion object {
        const val CURRENT = "/data/2.5/weather"
        const val FORECAST = "/data/2.5/forecast"
    }

    //Current Weather Endpoints start
    @GET(CURRENT)
    fun getCurrentWeatherByCityName(@QueryMap options: Map<String?, String?>?): Call<WeatherResponse?>?

    //Three hour forecast endpoints start
    @GET(FORECAST)
    fun getEightDayForecastByCityName(@QueryMap options: Map<String?, String?>?): Call<ForecastData?>?


}