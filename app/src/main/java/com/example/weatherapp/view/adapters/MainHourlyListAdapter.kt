package com.example.weatherapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.weatherapp.R
import com.example.weatherapp.business.model.HourlyWeatherModel
import com.example.weatherapp.databinding.ItemDayHourrlyBinding
import com.example.weatherapp.view.*

class MainHourlyListAdapter: BaseAdapter<HourlyWeatherModel>() {


    inner class HourlyViewHolder(val binding: ItemDayHourrlyBinding): BaseViewHolder(binding.root) {
        override fun bindView(position: Int) {
            mData[position].apply {
                binding.itemHourlyTimeTv.text = dt.toDayFormatOf(HOUR_DOUBLE_DOT_MINUTE)
                binding.itemHourlyTempTv.text = StringBuilder().append(temp.toDegree()).append("\u00B0")
                binding.itemHourlyPopTv.text = pop.toString()
                binding.itemHourlyImageview.setImageResource(weather[0].icon.provideIcon())
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        return HourlyViewHolder(ItemDayHourrlyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


}