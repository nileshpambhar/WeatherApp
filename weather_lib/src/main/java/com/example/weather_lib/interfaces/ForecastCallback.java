package com.example.weather_lib.interfaces;

import com.example.weather_lib.model.forecast.ForecastData;

public interface ForecastCallback {
    void onSuccess(ForecastData threeHourForecast);
    void onFailure(Throwable throwable);
}