package com.example.weatherapp.business

import com.example.weatherapp.business.model.WeatherDataModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/onecall?")
    fun getWeatherForecast(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude: String = "minutely,alerts",
        @Query("appid") apiid: String = "34ddc8e46592fa496e871e4d4a3fbae3",
        @Query("lang") lang: String = "en"
    ): Observable<WeatherDataModel>

}