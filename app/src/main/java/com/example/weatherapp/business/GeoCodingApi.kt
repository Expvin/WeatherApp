package com.example.weatherapp.business

import com.example.weatherapp.business.model.GeoCodeModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoCodingApi {

    @GET("geo/1.0/reverse?")
    fun getCityByCord(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("limit") limit: String = "10",
        @Query("appid") appid: String = "34ddc8e46592fa496e871e4d4a3fbae3"
    ) : Observable<List<GeoCodeModel>>
}