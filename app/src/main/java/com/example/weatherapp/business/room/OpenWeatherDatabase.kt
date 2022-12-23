package com.example.weatherapp.business.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeatherDataEntity::class, GeoCodeEntity::class], exportSchema = false, version = 2 )
abstract class OpenWeatherDatabase: RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDao

    abstract fun getGeoCodeDao(): GeoCodeDao
}