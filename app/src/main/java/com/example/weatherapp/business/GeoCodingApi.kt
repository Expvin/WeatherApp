package com.example.weatherapp.business

import com.example.weatherapp.business.model.GeoCodeModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoCodingApi {

    @GET("geo/1.0/direct")
    fun getCityByName(
        @Query("q") name: String,
        @Query("limit") limit: String = "10",
        @Query("appid") appid: String = "",
    ) : Observable<List<GeoCodeModel>>

    @GET("geo/1.0/reverse?")
    fun getCityByCord(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("limit") limit: String = "10",
        @Query("appid") id: String = ""
    ) : Observable<List<GeoCodeModel>>
}