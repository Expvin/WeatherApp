package com.example.weatherapp.business.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface WeatherDao  {

    @Query("SELECT * FROM WeatherDataDB WHERE id = 1")
    fun getWeatherData(): WeatherDataEntity

    @Update
    fun updateWeatherData(data: WeatherDataEntity)

    @Delete
    fun deleteWeatherData(data: WeatherDataEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeatherData(data: WeatherDataEntity)
}