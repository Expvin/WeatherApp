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
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.view.*
import com.example.weatherapp.view.adapters.MainDailyAdapter
import com.example.weatherapp.view.adapters.MainHourlyListAdapter
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.R
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter


const val TAG = "GEO_TEST"

class MainActivity : MvpAppCompatActivity(), MainView {

    private lateinit var binding: ActivityMainBinding

    private val mainPresent by moxyPresenter{ MainPresenter() }

    private val geoService by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val locationRequest by lazy { initLocationRequest() }
    private lateinit var mLocation: Location


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()

        binding.mainHourlyList.apply {
            adapter = MainHourlyListAdapter()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }

        binding.mainDallyList.apply {
            adapter = MainDailyAdapter()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }

        mainPresent.enable()

        geoService.requestLocationUpdates(locationRequest, geoCallback, null)
    }


    //--------------- init view ---------------

    private fun initViews() {
        binding.cityNameTv.text = "Moscow"
        binding.dateTv.text = "1 april"
        binding.mainTempTv.text = "25\u00B0"
        binding.minMainTempTv.text = "19"
        binding.medMainTempTv.text = "21"
        binding.maxMainTempTv.text = "28"
        binding.mainPressureTv.text = "1023 hPa"
        binding.mainHumidityTv.text = "88 %"
        binding.mainWindSpeedTv.text = "5 m/s"
        binding.mainSunriseTv.text = "4:30"
        binding.mainSunsetTv.text = "22:43"
    }

    //--------------- init view ---------------

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
        binding.cityNameTv.text = data
    }

    override fun displayCurrentData(data: WeatherDataModel) {
        data.apply {
            binding.dateTv.text = current.dt.toDayFormatOf(DAY_FULL_MONTH_NAME)
            binding.weatherConditionIcon.setImageResource(current.weather[0].icon.provideIcon())
            binding.mainTempTv.text = current.temp.toDegree()
            binding.mainWeatherDescription.text = current.weather[0].description
            daily[0].temp.apply {
                binding.maxMainTempTv.text = max.toDegree()
                binding.medMainTempTv.text = eve.toDegree()
                binding.minMainTempTv.text = min.toDegree()
            }
            binding.mainPressureTv.text = StringBuilder().append(current.pressure.toString()).append(" кПа")
            binding.mainHumidityTv.text = StringBuilder().append(current.humidity.toString()).append(" %")
            binding.mainWindSpeedTv.text = StringBuilder().append(current.wind_speed.toString()).append(" м/с")
            binding.mainSunriseTv.text = current.sunrise.toDayFormatOf(HOUR_DOUBLE_DOT_MINUTE)
            binding.mainSunsetTv.text = current.sunset.toDayFormatOf(HOUR_DOUBLE_DOT_MINUTE)
        }

    }

    override fun displayHourlyData(data: List<HourlyWeatherModel>) {
        (binding.mainHourlyList.adapter as MainHourlyListAdapter).updateData(data)
    }

    override fun displayDailyData(data: List<DailyWeatherModel>) {
        (binding.mainDallyList.adapter as MainDailyAdapter).updateData(data)
    }

    override fun displayError(error: Throwable?) {

    }

    override fun setLoading(flag: Boolean) {

    }


    //--------------- moxy code ---------------

    // https://api.openweathermap.org/data/2.5/onecall?lat=54.07328&lon=43.2461&exclude=minutely&appid=ca8d1939be2fc1f8cc73a2e515d9ad1f
}