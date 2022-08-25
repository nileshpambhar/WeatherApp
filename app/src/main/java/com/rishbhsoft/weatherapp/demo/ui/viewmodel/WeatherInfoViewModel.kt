package com.rishbhsoft.weatherapp.demo.ui.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_lib.helper.WeatherApiHelper
import com.example.weather_lib.interfaces.CurrentWeatherCallback
import com.example.weather_lib.model.WeatherResponse
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.rishbhsoft.weatherapp.demo.common.RequestCompleteListener
import com.rishbhsoft.weatherapp.demo.data.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class WeatherInfoViewModel(
    private val application: Application,
    private val weatherApiHelper: WeatherApiHelper
) : ViewModel() {

    val weatherInfoLiveData = MutableLiveData<WeatherResponse?>()
    val weatherInfoFailureLiveData = MutableLiveData<String>()
    val progressBarLiveData = MutableLiveData<Boolean>()


    fun getCityList( callback: RequestCompleteListener<MutableList<City>>) {
        try {
            val stream = application.assets.open("city_list.json")

            val size = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()
            val tContents = String(buffer)

            val groupListType = object : TypeToken<ArrayList<City>>() {}.type
            val gson = GsonBuilder().create()
            val cityList: MutableList<City> = gson.fromJson(tContents, groupListType)

            callback.onRequestSuccess(cityList)

        } catch (e: IOException) {
            e.printStackTrace()
            //let presenter know about failure
            callback.onRequestFailed(e.localizedMessage)
//            e.localizedMessage?.let { callback.onRequestFailed(it) };
        }

    }

    fun getWeatherInfo(cityName: String) {
        progressBarLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            weatherApiHelper.getCurrentWeatherByCityName(cityName, object : CurrentWeatherCallback {
                override fun onSuccess(currentWeather: WeatherResponse?) {
                    progressBarLiveData.postValue(false)
                    weatherInfoLiveData.postValue(currentWeather)
                }

                override fun onFailure(throwable: Throwable?) {
                    progressBarLiveData.postValue(false)
                    weatherInfoFailureLiveData.postValue(throwable?.localizedMessage)
                }
            })
        }

    }
}