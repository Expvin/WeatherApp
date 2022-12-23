package com.example.weatherapp.business.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GeoCodeDao {

    @Query("SELECT * FROM GeoCode")
    fun getAll(): List<GeoCodeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(item: GeoCodeEntity)

    @Delete
    fun remove(item: GeoCodeEntity)
}