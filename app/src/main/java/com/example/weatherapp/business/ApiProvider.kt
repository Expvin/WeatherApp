package com.example.weatherapp.business

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class ApiProvider {

    private val openWeatherMap: Retrofit by lazy { initApi() }

    private fun initApi() = Retrofit.Builder()
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://api.openweathermap.org/")
        .build()


    fun providerWeatherApi(): WeatherApi = openWeatherMap.create(WeatherApi::class.java)

    fun providerGeoCodeApi(): GeoCodingApi = openWeatherMap.create(GeoCodingApi::class.java)

}