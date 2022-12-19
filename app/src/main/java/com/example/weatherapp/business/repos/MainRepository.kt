package com.example.weatherapp.business.repos

import android.util.Log
import com.example.weatherapp.business.ApiProvider
import com.example.weatherapp.business.model.WeatherDataModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable

import io.reactivex.rxjava3.schedulers.Schedulers

const val TAG = "REPOSTIRY_TEST"
class MainRepository(api: ApiProvider): BaseRepository<MainRepository.ServerReponse>(api) {

    fun reloadData(lat: String, lon: String) {
        Observable.zip(
            api.providerWeatherApi().getWeatherForecast(lat, lon),
            api.providerGeoCodeApi().getCityByCord(lat, lon).map {
                it.asSequence()
                    .map { model -> model.name }
                    .toList()
                    .filterNotNull()
                    .first()
            }, {weaherData, geoCode -> ServerReponse(geoCode, weaherData)}
        )
            .subscribeOn(Schedulers.io())
            .doOnNext{ /* TODO тут будет добавление данных */}
           // .onErrorResumeNext { // TODO извлечение фаила из нашей базы данных }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    dataEmitter.onNext(it)
                }, {
                    Log.d(TAG, "reloadData: $it")
                }
            )
    }

    data class ServerReponse(
        val cityName: String,
        val weatherData: WeatherDataModel,
        val error: Throwable? = null
    )

}