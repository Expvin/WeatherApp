package com.example.weatherapp.business.repos

import android.util.Log
import com.example.weatherapp.App.Companion.db
import com.example.weatherapp.business.ApiProvider
import com.example.weatherapp.business.model.WeatherDataModel
import com.example.weatherapp.business.room.WeatherDataEntity
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

const val TAG = "GEO_TEST"
class MainRepository(api: ApiProvider): BaseRepository<MainRepository.ServerReponse>(api) {

    private val gson = Gson()
    private val dbAccess = db.getWeatherDao()
    private val defLanguage = when(Locale.getDefault().displayLanguage) {
        "русский" -> "ru"
        else -> "en"
    }

    fun reloadData(lat: String, lon: String) {
        Observable.zip(
            api.providerWeatherApi().getWeatherForecast(lat, lon, lang = defLanguage),
            api.providerGeoCodeApi().getCityByCord(lat, lon).map {
                it.asSequence()
                    .map { model ->
                        when (Locale.getDefault().displayLanguage) {
                            "русский" -> model.local_names.ru
                            "English" -> model.local_names.en
                            else -> model.name
                        }
                    }
                    .toList()
                    .filterNotNull()
                    .first()
            },
            { weatherData, geoCode -> ServerReponse(geoCode, weatherData) }
        )
            .subscribeOn(Schedulers.io())
            .doOnNext{dbAccess.insertWeatherData(WeatherDataEntity(data = gson.toJson(it.weatherData), city = it.cityName))}
            .onErrorResumeNext { Observable.just(ServerReponse(dbAccess.getWeatherData().city,
            gson.fromJson(dbAccess.getWeatherData().data, WeatherDataModel::class.java), it)) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    dataEmitter.onNext(it)
                }, {
                    Log.d(TAG, "reloadData: $it")
                })
    }
    data class ServerReponse(
        val cityName: String,
        val weatherData: WeatherDataModel,
        val error: Throwable? = null
    )
}