package com.example.weather_lib.interfaces;


import com.example.weather_lib.model.WeatherResponse;

public interface CurrentWeatherCallback{
    void onSuccess(WeatherResponse currentWeather);
    void onFailure(Throwable throwable);
}
