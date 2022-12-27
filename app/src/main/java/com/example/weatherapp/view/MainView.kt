package com.example.weatherapp.view
import android.location.Location
import com.example.weatherapp.business.model.DailyWeatherModel
import com.example.weatherapp.business.model.HourlyWeatherModel
import com.example.weatherapp.business.model.WeatherDataModel
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface MainView: MvpView {

    @AddToEndSingle
    fun displayLocation(data: String)

    @AddToEndSingle
    fun displayCurrentData(data: WeatherDataModel)

    @AddToEndSingle
    fun displayHourlyData(data: List<HourlyWeatherModel>)

    @AddToEndSingle
    fun displayDailyData(data: List<DailyWeatherModel>)

    @AddToEndSingle
    fun displayError(error: Throwable?)

    @AddToEndSingle
    fun setLoading(flag: Boolean)

}