## WeatherApp (MVVM + Livedata+ Kotlin + Retrofit)
Weather SDK
### Open Weather API
We will use [Open Weather Map API](https://openweathermap.org/api) for collecting weather information.

### SCREENSHOTS
<img src="https://github.com/nileshpambhar/WeatherApp/blob/master/assets/Screenshot_20220825_164332.png" width="250" height="444" />
<img src="https://github.com/nileshpambhar/WeatherApp/blob/master/assets/Screenshot_20220825_164401.png" width="250" height="444" />

## Usage

This SDK provide different method for weather data.

Initialize SDK

**SDK path and include as library module**

~~~
   implementation project(path: ':weather_lib')
~~~
   
~~~
val weatherApiHelper:WeatherApiHelper = WeatherApiHelper(BuildConfig.APP_ID) 
~~~

### SDK Methods
 - getCurrentWeatherByCityName
 - getCurrentWeatherByCityName

## Get Current Weather
~~~
public void getCurrentWeatherByCityName(String city, final CurrentWeatherCallback callback)
~~~

## Get Forecast Weather
~~~
public void getCurrentWeatherByCityName(String city,int days, final ForecastCallback callback)
~~~

To run sample application:
set **app_id** in **local.properties** file which is **API_KEY** provided by Open Weathe Map.

Sample code for getting weather info

#### MainActivity.kt
 ~~~
  private fun setViewClickListener() {
        // View Weather button click listener
        binding.layoutInput.btnViewWeather.setOnClickListener {
            val cityName = cityList[binding.layoutInput.spinner.selectedItemPosition].name
            viewModel.getWeatherInfo(cityName) // fetch weather info
        }
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
~~~

#### WeatherInfoViewModel.kt
~~~
  fun getWeatherInfo(cityName: String) {
        progressBarLiveData.postValue(true)
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
~~~

### Run the project
Sync the `Gradle` and run the project. Install APK on your emulator or real device. Turn on the internet of your testing device


