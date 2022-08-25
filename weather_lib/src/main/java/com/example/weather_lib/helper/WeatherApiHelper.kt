package com.example.weather_lib.helper

import com.example.weather_lib.interfaces.CurrentWeatherCallback
import com.example.weather_lib.interfaces.ForecastCallback
import com.example.weather_lib.model.WeatherResponse
import com.example.weather_lib.model.forecast.ForecastData
import com.example.weather_lib.network.RetrofitClient.getInstance
import com.example.weather_lib.network.WeatherApiService
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection

class WeatherApiHelper(apiKey: String?) {
    private val weatherApiService: WeatherApiService =
        getInstance().create(WeatherApiService::class.java)
    private val options: MutableMap<String?, String?>

    init {
        options = HashMap()
        options[APPID] = apiKey ?: ""
    }

    //SETUP METHODS
    fun setUnits(units: String?) {
        options[UNITS] = units
    }

    fun setLanguage(lang: String?) {
        options[LANGUAGE] = lang
    }

    private fun NoAppIdErrMessage(): Throwable {
        return Throwable("UnAuthorized. Please set a valid OpenWeatherMap API KEY.")
    }

    private fun NotFoundErrMsg(str: String): Throwable {
        var throwable: Throwable? = null
        try {
            val obj = JSONObject(str)
            throwable = Throwable(obj.getString("message"))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        if (throwable == null) {
            throwable = Throwable("An unexpected error occurred.")
        }
        return throwable
    }

    /**
     * GET CURRENT WEATHER BY CITY NAME
     * @param city
     * @param callback
     */
    fun getCurrentWeatherByCityName(city: String?, callback: CurrentWeatherCallback) {
        options[QUERY] = city
        weatherApiService.getCurrentWeatherByCityName(options)!!
            .enqueue(object : Callback<WeatherResponse?> {

                override fun onResponse(
                    call: Call<WeatherResponse?>,
                    response: Response<WeatherResponse?>
                ) {
                    handleCurrentWeatherResponse(response, callback)
                }

                override fun onFailure(call: Call<WeatherResponse?>, throwable: Throwable) {
                    callback.onFailure(throwable)
                }
            })
    }

    /**
     *
     * @param response
     * @param callback
     */
    private fun handleCurrentWeatherResponse(
        response: Response<WeatherResponse?>,
        callback: CurrentWeatherCallback
    ) {
        if (response.code() == HttpURLConnection.HTTP_OK) {
            callback.onSuccess(response.body())
        } else if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            callback.onFailure(NoAppIdErrMessage())
        } else {
            try {
                callback.onFailure(NotFoundErrMsg(response.errorBody()!!.string()))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * GET THREE HOUR FORECAST BY CITY NAME, API RETURNS FORECAST FOR EVERY 3 HOURS
     * @param city
     * @param days
     * @param callback
     */
    fun getForecastByCityName(city: String?, days: Int, callback: ForecastCallback) {
        options[QUERY] = city
        weatherApiService.getEightDayForecastByCityName(options)
            ?.enqueue(object : Callback<ForecastData?> {
                override fun onResponse(
                    call: Call<ForecastData?>,
                    response: Response<ForecastData?>
                ) {
                    handleThreeHourForecastResponse(response, callback)
                }

                override fun onFailure(call: Call<ForecastData?>, throwable: Throwable) {
                    callback.onFailure(throwable)
                }
            })
    }

    /**
     *
     * @param response
     * @param callback
     */
    private fun handleThreeHourForecastResponse(
        response: Response<ForecastData?>,
        callback: ForecastCallback
    ) {
        if (response.code() == HttpURLConnection.HTTP_OK) {
            callback.onSuccess(response.body())
        } else if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            callback.onFailure(NoAppIdErrMessage())
        } else {
            try {
                callback.onFailure(NotFoundErrMsg(response.errorBody()!!.string()))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private const val APPID = "appId"
        private const val UNITS = "units"
        private const val LANGUAGE = "lang"
        private const val QUERY = "q"
        private const val COUNT = "CNT"
        private const val CITY_ID = "id"
        private const val LATITUDE = "lat"
        private const val LONGITUDE = "lon"
        private const val ZIP_CODE = "zip"
    }


}