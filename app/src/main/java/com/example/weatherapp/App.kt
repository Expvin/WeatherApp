package com.example.weatherapp

import android.app.Application
import android.content.Intent


const val APP_SETTINGS = "App Settings"
const val IS_START_UP = "is start up"
class App: Application() {

    override fun onCreate() {
        super.onCreate()

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