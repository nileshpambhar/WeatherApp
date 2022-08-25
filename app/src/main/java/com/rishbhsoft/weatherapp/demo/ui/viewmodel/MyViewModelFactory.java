package com.rishbhsoft.weatherapp.demo.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.weather_lib.helper.WeatherApiHelper;

public class MyViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private WeatherApiHelper weatherApiHelper;


    public MyViewModelFactory(Application application, WeatherApiHelper mweatherApiHelper) {
        mApplication = application;
        weatherApiHelper = mweatherApiHelper;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new WeatherInfoViewModel(mApplication, weatherApiHelper);
    }
}