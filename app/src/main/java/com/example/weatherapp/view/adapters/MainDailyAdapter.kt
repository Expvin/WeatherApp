package com.example.weatherapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.weatherapp.R
import com.example.weatherapp.business.model.DailyWeatherModel
import com.example.weatherapp.databinding.ItemMainDailyBinding
import com.example.weatherapp.view.DAY_WEEK_NAME_LONG
import com.example.weatherapp.view.provideIcon
import com.example.weatherapp.view.toDayFormatOf
import com.example.weatherapp.view.toDegree

class MainDailyAdapter: BaseAdapter<DailyWeatherModel>() {

    inner class DailyViewHolder(val binding: ItemMainDailyBinding): BaseViewHolder(binding.root) {
        override fun bindView(position: Int) {
            mData[position].apply {
                binding.itemDailyDateTv.text = dt.toDayFormatOf(DAY_WEEK_NAME_LONG)
                binding.itemDailyWeatherConditionIcon.setImageResource(weather[0].icon.provideIcon())
                binding.itemDailyMaxTempTv.text = StringBuilder().append(temp.max.toDegree()).append("°")
                binding.itemDailyMedTempTv.text = StringBuilder().append(temp.eve.toDegree()).append("°")
                binding.itemDailyMinTempTv.text = StringBuilder().append(temp.min.toDegree()).append("°")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        return DailyViewHolder(ItemMainDailyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

}