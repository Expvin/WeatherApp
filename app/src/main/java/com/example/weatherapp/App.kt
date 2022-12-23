package com.example.weatherapp

import android.app.Application
import android.content.Intent
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.business.room.OpenWeatherDatabase


const val APP_SETTINGS = "App Settings"
const val IS_START_UP = "is start up"
class App: Application() {

    companion object {
        lateinit var db: OpenWeatherDatabase
    }

    override fun onCreate() {
        super.onCreate()
        // TODO Убрать fallbackToDestructiveMigration()
        db = Room.databaseBuilder(this, OpenWeatherDatabase::class.java, "OpenWeatherDB")
            .fallbackToDestructiveMigration()
            .build()
        val preferences = getSharedPreferences(APP_SETTINGS, MODE_PRIVATE)
        val flag = preferences.contains(IS_START_UP)

        if (!flag) {
            val editor = preferences.edit()
            editor.putBoolean(IS_START_UP, true)
            editor.apply()
            val intent = Intent(this, InitialActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

    }

}