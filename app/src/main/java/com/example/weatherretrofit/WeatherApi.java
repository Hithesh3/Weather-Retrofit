package com.example.weatherretrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {

    @GET("weather")
    Call<WeatherModel> getWeatherModel(@Query("q") String city,
                                       @Query("appid") String app);

}
