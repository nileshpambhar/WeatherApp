package com.example.weather_lib.helper;


import com.example.weather_lib.interfaces.CurrentWeatherCallback;
import com.example.weather_lib.interfaces.ForecastCallback;
import com.example.weather_lib.model.WeatherResponse;
import com.example.weather_lib.model.forecast.ForecastData;
import com.example.weather_lib.network.WeatherApiService;
import com.example.weather_lib.network.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherApiHelper {

    private static final String APPID = "appId";
    private static final String UNITS = "units";
    private static final String LANGUAGE = "lang";
    private static final String QUERY = "q";
    private static final String COUNT = "CNT";
    private static final String CITY_ID = "id";
    private static final String LATITUDE = "lat";
    private static final String LONGITUDE = "lon";
    private static final String ZIP_CODE = "zip";

    private final WeatherApiService weatherApiService;
    private final Map<String, String> options;


    public WeatherApiHelper(String apiKey){
        weatherApiService = RetrofitClient.INSTANCE.getInstance().create(WeatherApiService.class);
        options = new HashMap<>();
        options.put(APPID, apiKey == null ? "" : apiKey);
    }


    //SETUP METHODS
    public void setUnits(String units){
        options.put(UNITS, units);
    }
    public void setLanguage(String lang) {
        options.put(LANGUAGE, lang);
    }


    private Throwable NoAppIdErrMessage() {
        return new Throwable("UnAuthorized. Please set a valid OpenWeatherMap API KEY.");
    }


    private Throwable NotFoundErrMsg(String str) {
        Throwable throwable = null;
        try {
            JSONObject obj = new JSONObject(str);
            throwable = new Throwable(obj.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (throwable == null){
            throwable = new Throwable("An unexpected error occurred.");
        }


        return throwable;
    }

    /**
     * GET CURRENT WEATHER BY CITY NAME
     * @param city
     * @param callback
     */
    public void getCurrentWeatherByCityName(String city, final CurrentWeatherCallback callback){
        options.put(QUERY, city);

        weatherApiService.getCurrentWeatherByCityName(options).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse( Call<WeatherResponse> call,  Response<WeatherResponse> response) {
                handleCurrentWeatherResponse(response, callback);
            }

            @Override
            public void onFailure( Call<WeatherResponse> call,  Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    /**
     *
     * @param response
     * @param callback
     */
    private void handleCurrentWeatherResponse(Response<WeatherResponse> response, CurrentWeatherCallback callback){
        if (response.code() == HttpURLConnection.HTTP_OK){
            callback.onSuccess(response.body());
        }
        else if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED){
            callback.onFailure(NoAppIdErrMessage());
        }
        else{
            try {
                callback.onFailure(NotFoundErrMsg(response.errorBody().string()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * GET THREE HOUR FORECAST BY CITY NAME, API RETURNS FORECAST FOR EVERY 3 HOURS
     * @param city
     * @param days
     * @param callback
     */
    public void getForecastByCityName(String city,int days, final ForecastCallback callback){
        options.put(QUERY, city);
        weatherApiService.getEightDayForecastByCityName(options)
                .enqueue(new Callback<ForecastData>() {
                    @Override
                    public void onResponse(Call<ForecastData> call,  Response<ForecastData> response) {
                        handleThreeHourForecastResponse(response, callback);
                    }

                    @Override
                    public void onFailure( Call<ForecastData> call, Throwable throwable) {
                        callback.onFailure(throwable);
                    }
                });

    }


    /**
     *
     * @param response
     * @param callback
     */
    private void handleThreeHourForecastResponse(Response<ForecastData> response, ForecastCallback callback){
        if (response.code() == HttpURLConnection.HTTP_OK){
            callback.onSuccess(response.body());
        }
        else if (response.code() == HttpURLConnection.HTTP_FORBIDDEN || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED){
            callback.onFailure(NoAppIdErrMessage());
        }
        else{
            try {
                callback.onFailure(NotFoundErrMsg(response.errorBody().string()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
