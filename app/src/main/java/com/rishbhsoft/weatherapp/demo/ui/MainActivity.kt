package com.rishbhsoft.weatherapp.demo.ui

import android.os.Bundle
import android.provider.UserDictionary.Words.APP_ID
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.weather_lib.helper.WeatherApiHelper
import com.example.weather_lib.model.WeatherResponse
import com.rishbhsoft.weatherapp.demo.BuildConfig
import com.rishbhsoft.weatherapp.demo.R
import com.rishbhsoft.weatherapp.demo.common.RequestCompleteListener
import com.rishbhsoft.weatherapp.demo.data.City
import com.rishbhsoft.weatherapp.demo.databinding.ActivityMainBinding
import com.rishbhsoft.weatherapp.demo.ui.viewmodel.MyViewModelFactory
import com.rishbhsoft.weatherapp.demo.ui.viewmodel.WeatherInfoViewModel
import com.rishbhsoft.weatherapp.demo.utils.convertMtrToKiloPerHour
import com.rishbhsoft.weatherapp.demo.utils.convertToListOfCityName
import com.rishbhsoft.weatherapp.demo.utils.kelvinToCelsius
import com.rishbhsoft.weatherapp.demo.utils.unixTimestampToDateTimeString

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<WeatherInfoViewModel> {
        MyViewModelFactory(
            application,
            WeatherApiHelper(BuildConfig.APP_ID)
        )
    }

    private var cityList: MutableList<City> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setLivedataListeners()
        setViewClickListener()
        loadCity()
    }

    private fun loadCity() {
        binding.progressBar.visibility = View.VISIBLE;
        viewModel.getCityList( object :
            RequestCompleteListener<MutableList<City>> {
            override fun onRequestSuccess(data: MutableList<City>) {
                binding.progressBar.visibility = View.INVISIBLE;
                setCityListSpinner(data)

            }

            override fun onRequestFailed(errorMessage: String) {
                binding.progressBar.visibility = View.INVISIBLE;

            }
        })
    }

    private fun setViewClickListener() {
        // View Weather button click listener
        binding.layoutInput.btnViewWeather.setOnClickListener {
            val cityName = cityList[binding.layoutInput.spinner.selectedItemPosition].name
            viewModel.getWeatherInfo(cityName) // fetch weather info
        }
    }

    private fun setLivedataListeners() {

        viewModel.progressBarLiveData.observe(this, Observer { isShowLoader ->
            if (isShowLoader) {
                binding.progressBar.visibility = View.VISIBLE;
            } else {
                binding.progressBar.visibility = View.INVISIBLE;
            }
        })

        viewModel.weatherInfoLiveData.observe(this, Observer { weatherData ->
            weatherData?.let { setWeatherInfo(it) }
        })

        viewModel.weatherInfoFailureLiveData.observe(this) {
            Toast.makeText(this@MainActivity, it, Toast.LENGTH_LONG).show()
        }
    }

    private fun setCityListSpinner(cityList: MutableList<City>) {
        this.cityList = cityList

        val arrayAdapter = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            this.cityList.convertToListOfCityName()
        )

        binding.layoutInput.spinner.adapter = arrayAdapter
    }

    private fun setWeatherInfo(weatherData: WeatherResponse) {

        binding.layoutInfo.tvDateTime.text = weatherData.dt.unixTimestampToDateTimeString()
        binding.layoutInfo.tvTemperature.text = weatherData.main.temp.kelvinToCelsius().toString()
        binding.layoutInfo.tvCityCountry.text = "${weatherData.name}, ${weatherData.sys.country}"
        Glide.with(this).load("http://openweathermap.org/img/w/${weatherData.weather[0].icon}.png")
            .into(binding.layoutInfo.ivWeatherCondition)
        binding.layoutInfo.tvWeatherCondition.text = weatherData.weather[0].description
        binding.layoutWeatherAdditional.tvHumidityValue.text = "${weatherData.main.humidity}%"
        binding.layoutWeatherAdditional.tvPressureValue.text = "${weatherData.main.pressure} mBar"
        binding.layoutWeatherAdditional.tvVisibilityValue.text =
            "${weatherData.visibility / 1000} KM"
        binding.layoutWeatherAdditional.tvWindValue.text =
            "${weatherData.wind.speed.convertMtrToKiloPerHour()} KM/h"

    }
}