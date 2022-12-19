package com.example.weatherapp

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.business.model.DailyWeatherModel
import com.example.weatherapp.business.model.HourlyWeatherModel
import com.example.weatherapp.business.model.WeatherDataModel
import com.example.weatherapp.business.presenters.MainPresenter
import com.example.weatherapp.view.MainView
import com.example.weatherapp.view.adapters.MainDailyAdapter
import com.example.weatherapp.view.adapters.MainHourlyListAdapter
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter


const val TAG = "GEO_TEST"

class MainActivity : MvpAppCompatActivity(), MainView {

    private val mainPresent by moxyPresenter{ MainPresenter() }

    private val geoService by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val locationRequest by lazy { initLocationRequest() }
    private lateinit var mLocation: Location


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        main_hourly_list.apply {
            adapter = MainHourlyListAdapter()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }

        main_dally_list.apply {
            adapter = MainDailyAdapter()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }

        mainPresent.enable()

        geoService.requestLocationUpdates(locationRequest, geoCallback, null)
    }

    //---------------location code---------------

    private fun initLocationRequest(): LocationRequest {
        val request = LocationRequest.create()
        return request.apply {
            interval = 10_000
            fastestInterval = 5_000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private val geoCallback = object: LocationCallback() {
        override fun onLocationResult(geo: LocationResult) {
            super.onLocationResult(geo)
            for (location in geo.locations) {
                mLocation = location
                mainPresent.refresh(mLocation.latitude.toString(), mLocation.longitude.toString())
                Log.d(TAG, "onLocationResult: lat-${location.latitude} lon-${location.longitude}")
            }
        }
    }

    //---------------location code---------------

    //--------------- moxy code ---------------

    override fun displayLocation(data: String) {

    }

    override fun displayCurrentData(data: WeatherDataModel) {

    }

    override fun displayHourlyData(data: List<HourlyWeatherModel>) {
        (main_hourly_list.adapter as MainHourlyListAdapter).updateData(data)
    }

    override fun displayDailyData(data: List<DailyWeatherModel>) {
        (main_dally_list.adapter as MainDailyAdapter).updateData(data)
    }

    override fun displayError(error: Throwable?) {

    }

    override fun setLoading(flag: Boolean) {

    }


    //--------------- moxy code ---------------

    // https://api.openweathermap.org/data/2.5/onecall?lat=54.07328&lon=43.2461&exclude=minutely&appid=ca8d1939be2fc1f8cc73a2e515d9ad1f
}