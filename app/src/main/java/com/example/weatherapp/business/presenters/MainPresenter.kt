package com.example.weatherapp.business.presenters

import android.util.Log
import com.example.weatherapp.business.ApiProvider
import com.example.weatherapp.business.repos.MainRepository
import com.example.weatherapp.business.repos.TAG
import com.example.weatherapp.view.MainView

const val TAG = "GEO_TEST"
class MainPresenter: BasePresenter<MainView>() {

    private val repo = MainRepository(ApiProvider())

    override fun enable() {
        repo.dataEmitter.subscribe { response ->
            Log.d(TAG, "Presenter_enable() $response")
            viewState.displayLocation(response.cityName)
            viewState.displayCurrentData(response.weatherData)
            viewState.displayDailyData(response.weatherData.daily)
            viewState.displayHourlyData(response.weatherData.hourly)
            response.error?.let { viewState.displayError(response.error) }

        }
    }

    fun refresh(lat: String, lot: String) {
        viewState.setLoading(true)
        repo.reloadData(lat, lot)
    }
}