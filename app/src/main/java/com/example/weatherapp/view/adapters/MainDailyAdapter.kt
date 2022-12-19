package com.example.weatherapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.weatherapp.R
import com.example.weatherapp.business.model.DailyWeatherModel
import com.example.weatherapp.databinding.ItemMainDailyBinding

class MainDailyAdapter: BaseAdapter<DailyWeatherModel>() {

    inner class DailyViewHolder(val binding: ItemMainDailyBinding): BaseViewHolder(binding.root) {
        override fun bindView(position: Int) {
            binding.itemDailyDateTv.text = "20 april"
            binding.itemDailyWeatherConditionIcon.setImageResource(R.drawable.sun)
            binding.itemDailyPopTv.text = "45%"
            binding.itemDailyMinTempTv.text = "15"
            binding.itemDailyMedTempTv.text = "20"
            binding.itemDailyMaxTempTv.text = "25"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        return DailyViewHolder(ItemMainDailyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = 25

}