package com.example.weatherapp.business.model

data class WeatherDataModel(
    val alerts: List<Alert>,
    val current: Current,
    val daily: List<DailyWeatherModel>,
    val hourly: List<HourlyWeatherModel>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int
)