package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weatherapp.databinding.ActivitySettingsBinding
import com.example.weatherapp.view.SettingsHolder
import com.google.android.material.button.MaterialButtonToggleGroup

class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.innerToolbar.setOnClickListener { onBackPressed() }

        setSavingSettings()
        listOf(binding.groupTemp, binding.groupWindSpeed, binding.groupPressure).forEach {
            it.addOnButtonCheckedListener(
                ToggleButtonClickListener
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SettingsHolder.onDestroy()
    }

    private fun setSavingSettings() {
        binding.groupTemp.check(SettingsHolder.temp.checkedViewId)
        binding.groupWindSpeed.check(SettingsHolder.windSpeed.checkedViewId)
        binding.groupPressure.check(SettingsHolder.pressure.checkedViewId)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_rigth)
    }

    private object ToggleButtonClickListener: MaterialButtonToggleGroup.OnButtonCheckedListener {
        override fun onButtonChecked(
            group: MaterialButtonToggleGroup?,
            checkedId: Int,
            isChecked: Boolean
        ) {
            when (checkedId to isChecked) {
                R.id.degreeC to true -> SettingsHolder.temp = SettingsHolder.Setting.TEMP_CELSIUS
                R.id.degreeF to true -> SettingsHolder.temp = SettingsHolder.Setting.TEMP_FAHRENHEIT
                R.id.speed_ms to true -> SettingsHolder.windSpeed = SettingsHolder.Setting.WIND_SPEED_MS
                R.id.speed_kmh to true -> SettingsHolder.windSpeed = SettingsHolder.Setting.WIND_SPEED_KMH
                R.id.pressure_mmHg to true -> SettingsHolder.pressure = SettingsHolder.Setting.PRESSURE_MMHG
                R.id.pressure_hPa to true -> SettingsHolder.pressure = SettingsHolder.Setting.PRESSURE_HPA
            }
        }

    }

}