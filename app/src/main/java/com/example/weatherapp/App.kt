package com.example.weatherapp

import android.app.Application
import androidx.room.Room
import com.example.weatherapp.business.room.OpenWeatherDatabase
import com.example.weatherapp.view.SettingsHolder


const val APP_SETTINGS = "App Settings"
class App: Application() {

    companion object {
        lateinit var db: OpenWeatherDatabase
    }

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(this, OpenWeatherDatabase::class.java, "OpenWeatherDB")
            .build()
        val preferences = getSharedPreferences(APP_SETTINGS, MODE_PRIVATE)
        SettingsHolder.onCreate(preferences)
    }

}