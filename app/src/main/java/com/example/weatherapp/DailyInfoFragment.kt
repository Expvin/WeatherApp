package com.example.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherapp.business.model.DailyWeatherModel
import com.example.weatherapp.databinding.FragmentDailyInfoBinding
import com.example.weatherapp.view.*


class DailyInfoFragment : DailyBaseFragment<DailyWeatherModel>() {

    private lateinit var viewContext: Context
    lateinit var binding: FragmentDailyInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.slide_rigth)
        enterTransition = inflater.inflateTransition(R.transition.slide_rigth)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDailyInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            fm.popBackStack()
        }
        viewContext = view.context
        updateView()
    }



    @SuppressLint("ResourceType")
    override fun updateView() {

        mData?.apply {
            binding.dayDate.text = dt.toDayFormatOf(DAY_FULL_MONTH_NAME)
            binding.dayTemp.text = viewContext.getString(R.string.degree_symbol, temp.getAverage().toDegree())
            binding.dayIcon.setImageResource(weather[0].icon.provideIcon())
            binding.dayMornTemp.text = viewContext.getString(R.string.degree_symbol, temp.morn.toDegree())
            binding.dayMornFl.text = viewContext.getString(R.string.degree_symbol, feels_like.morn.toDegree())
            binding.dayDailyTemp.text = viewContext.getString(R.string.degree_symbol, temp.day.toDegree())
            binding.dayDailyFl.text = viewContext.getString(R.string.degree_symbol, feels_like.day.toDegree())
            binding.dayEveTemp.text = viewContext.getString(R.string.degree_symbol, temp.eve.toDegree())
            binding.dayEveFl.text = viewContext.getString(R.string.degree_symbol, feels_like.eve.toDegree())
            binding.dayNightFl.text = viewContext.getString(R.string.degree_symbol, feels_like.night.toDegree())
            binding.dayNightTemp.text = viewContext.getString(R.string.degree_symbol, temp.night.toDegree())
            binding.dayHumidity.text = ("$humidity %")
            val settingPressure = SettingsHolder.pressure
            binding.dayPressure.text = viewContext.getString(settingPressure.mesureUnitStringRes,settingPressure.getValue(pressure.toDouble()))
            val settingWindSpeed = SettingsHolder.windSpeed
            binding.dayWindSpeed.text = viewContext.getString(settingWindSpeed.mesureUnitStringRes,settingWindSpeed.getValue(wind_speed))
            binding.dayWindDir.text = wind_deg.toString()
            binding.daySunrise.text = sunrise.toDayFormatOf(HOUR_DOUBLE_DOT_MINUTE)
            binding.daySunset.text = sunset.toDayFormatOf(HOUR_DOUBLE_DOT_MINUTE)
        }

    }

}