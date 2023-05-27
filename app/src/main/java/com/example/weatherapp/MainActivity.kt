package com.example.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Point
import android.location.Location
import android.media.ResourceBusyException
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.Extensions.isPermissionGranted
import com.example.weatherapp.business.model.DailyWeatherModel
import com.example.weatherapp.business.model.HourlyWeatherModel
import com.example.weatherapp.business.model.WeatherDataModel
import com.example.weatherapp.presenters.MainPresenter
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.view.*
import com.example.weatherapp.view.adapters.MainDailyAdapter
import com.example.weatherapp.view.adapters.MainHourlyListAdapter
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import kotlin.math.roundToInt


const val TAG = "GEO_TEST"
const val COORDINATES = "Coordinates"

class MainActivity : MvpAppCompatActivity(), MainView {
    private lateinit var binding: ActivityMainBinding
    private val tokenSource: CancellationTokenSource = CancellationTokenSource()
    private val mainPresent by moxyPresenter { MainPresenter() }
    private val geoService by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val locationRequest by lazy {
        LocationRequest.create().apply {
            interval = 600_000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private val geoCallback = object : LocationCallback() {
        override fun onLocationResult(geo: LocationResult) {
            super.onLocationResult(geo)
            for (location in geo.locations) {
                mLocation = location
                mainPresent.refresh(mLocation.latitude.toString(), mLocation.longitude.toString())
                Log.d(TAG, "onLocationResult: lat-${location.latitude} lon-${location.longitude}")
            }
        }
    }

    private lateinit var mLocation: Location

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkGeoAvailability()
        getGeo()
        geoService.requestLocationUpdates(locationRequest, geoCallback, null)

        initBottomSheets()
        initSwipeRefresh()

        supportFragmentManager.beginTransaction().add(
            R.id.fragment_container,
            DailyListFragment(),
            DailyListFragment::class.simpleName
        ).commit()

        binding.mainHourlyList.apply {
            adapter = MainHourlyListAdapter()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }

        if (!intent.hasExtra(COORDINATES)) {
            checkGeoAvailability()
            getGeo()
            geoService.requestLocationUpdates(locationRequest, geoCallback, null)
        } else {
            val coord = intent.extras!!.getBundle(COORDINATES)!!
            val loc = Location("")
            loc.latitude = coord.getString("lat")!!.toDouble()
            loc.longitude = coord.getString("lon")!!.toDouble()
            mLocation = loc
            mainPresent.refresh(
                lat = mLocation.latitude.toString(), lot = mLocation.longitude.toString()
            )
        }

        binding.mainMenuBth.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, android.R.anim.fade_out)
        }

        binding.mainSettingsBth.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_rigth, android.R.anim.fade_in)
        }

        mainPresent.enable()

    }

    override fun onRestart() {
        super.onRestart()
        mainPresent.refresh(mLocation.latitude.toString(), mLocation.longitude.toString())
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            ), LOCATION_RC
        )
    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
//    ) {
//        if (requestCode == LOCATION_RC && grantResults.isNotEmpty()) {
//            val permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED
//            if (permissionGranted) {
//                startActivity(Intent(this, MainActivity::class.java))
//                finish()
//            } else {
//                MaterialAlertDialogBuilder(this).setTitle(R.string.request_permission)
//                    .setMessage(R.string.dialog_text).setPositiveButton("Ok") { _, _ ->
//                        ActivityCompat.requestPermissions(
//                            this,
//                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                            GEO_LOCATION_REQUEST_COD_SUCCESS
//                        )
//                        ActivityCompat.requestPermissions(
//                            this,
//                            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
//                            GEO_LOCATION_REQUEST_COD_SUCCESS
//                        )
//                        startActivity(Intent(this, MainActivity::class.java))
//                        finish()
//                    }.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }.create().show()
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }


    override fun displayLocation(data: String) {
        binding.cityNameTv.text = data
    }

    @SuppressLint("ResourceType")
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
            val pressureSet = SettingsHolder.pressure
            binding.mainPressureTv.text = getString(
                pressureSet.mesureUnitStringRes, pressureSet.getValue(current.pressure.toDouble())
            )
            val windSpeedSet = SettingsHolder.windSpeed
            binding.mainWindSpeedTv.text = getString(
                windSpeedSet.mesureUnitStringRes, windSpeedSet.getValue(current.wind_speed)
            )
            binding.mainHumidityTv.text =
                StringBuilder().append(current.humidity.toString()).append(" %")

            binding.mainSunriseTv.text = current.sunrise.toDayFormatOf(HOUR_DOUBLE_DOT_MINUTE)
            binding.mainSunsetTv.text = current.sunset.toDayFormatOf(HOUR_DOUBLE_DOT_MINUTE)
        }

    }

    override fun displayHourlyData(data: List<HourlyWeatherModel>) {
        (binding.mainHourlyList.adapter as MainHourlyListAdapter).updateData(data)
    }

    override fun displayDailyData(data: List<DailyWeatherModel>) {
        (supportFragmentManager.findFragmentByTag(DailyListFragment::class.simpleName) as DailyListFragment).setData(
                data
            )
    }

    override fun displayError(error: Throwable?) {
//        Toast.makeText(this@MainActivity, "Ошибка", Toast.LENGTH_SHORT).show()
        Log.d("MainActivity", "displayError: $error ")
    }

    override fun setLoading(flag: Boolean) {
        binding.refresh.isRefreshing = flag
    }

    private fun initBottomSheets() {
        binding.mainBottomSheets.isNestedScrollingEnabled = true
        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        binding.mainBottomSheetsContainer.layoutParams =
            CoordinatorLayout.LayoutParams(size.x, (size.y * 0.5).roundToInt())
    }

    @SuppressLint("ResourceAsColor")
    private fun initSwipeRefresh() {
        binding.refresh.apply {
            setColorSchemeColors(R.color.purple_500)
            setProgressViewEndTarget(false, 280)
            setOnRefreshListener {
                try {
                    mainPresent.refresh(
                        mLocation.latitude.toString(), mLocation.longitude.toString()
                    )
                } catch (e: kotlin.UninitializedPropertyAccessException) {
                    requestPermission()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getGeo() {
        geoService.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, tokenSource.token)
            .addOnSuccessListener {
                if (it != null) {
                    mLocation = it
                    mainPresent.refresh(
                        mLocation.latitude.toString(), mLocation.longitude.toString()
                    )
                }
            }
    }

    private fun checkGeoAvailability() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(this, 100)
                } catch (sendEx: IntentSender.SendIntentException) {

                }
            }
        }
    }

    companion object {
        private const val LOCATION_RC = 111
    }

}

// https://api.openweathermap.org/data/2.5/onecall?lat=54.07328&lon=43.2461&exclude=minutely&appid=ca8d1939be2fc1f8cc73a2e515d9ad1f
