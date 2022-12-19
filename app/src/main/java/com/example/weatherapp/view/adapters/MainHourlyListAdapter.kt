package com.example.weatherapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.weatherapp.R
import com.example.weatherapp.business.model.HourlyWeatherModel
import com.example.weatherapp.databinding.ItemDayHourrlyBinding

class MainHourlyListAdapter: BaseAdapter<HourlyWeatherModel>() {


    inner class HourlyViewHolder(val binding: ItemDayHourrlyBinding): BaseViewHolder(binding.root) {
        override fun bindView(position: Int) {
            binding.itemHourlyTimeTv.text = "15 : 00"
            binding.itemHourlyTempTv.text = "24\u00B0"
            binding.itemHourlyImageview.setImageResource(R.drawable.sun)
            binding.itemHourlyPopTv.text = "45%"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        return HourlyViewHolder(ItemDayHourrlyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun getItemCount(): Int = 25
}