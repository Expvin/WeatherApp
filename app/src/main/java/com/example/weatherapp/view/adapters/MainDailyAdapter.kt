package com.example.weatherapp.view.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.weatherapp.R
import com.example.weatherapp.business.model.DailyWeatherModel
import com.example.weatherapp.databinding.ItemMainDailyBinding
import com.example.weatherapp.view.*

class MainDailyAdapter: BaseAdapter<DailyWeatherModel>() {

    lateinit var clickListener: DayItemClick

    inner class DailyViewHolder(val binding: ItemMainDailyBinding): BaseViewHolder(binding.root) {
        override fun bindView(position: Int) {
            val itemData = mData[position]
            val defaultTextColor = binding.itemDailyDateTv.textColors
            if (position == 0) binding.itemDailyDateTv.setTextColor(ContextCompat.getColor(binding.itemDailyDateTv.context, R.color.purple_500))
            else binding.itemDailyDateTv.setTextColor(defaultTextColor)
            binding.dayContainer.setOnClickListener { clickListener.showDetails(itemData) }
            if (mData.isNotEmpty()) {
                itemData.apply {
                    val dateOfDay = dt.toDayFormatOf(DAY_WEEK_NAME_LONG)
                    binding.itemDailyDateTv.text = if (dateOfDay.startsWith("0", true)) dateOfDay.removePrefix("0") else dateOfDay

                    if (pop < 0.01) binding.itemDailyPopTv.visibility = View.INVISIBLE
                    else {
                        binding.itemDailyPopTv.visibility = View.VISIBLE
                        binding.itemDailyPopTv.text = pop.toPercentString("%")
                    }
                    binding.itemDailyWeatherConditionIcon.setImageResource(weather[0].icon.provideIcon())
                    binding.itemDailyMinTempTv.text = itemView.context.getString(R.string.degree_symbol, temp.min.toDegree())
                    binding.itemDailyMedTempTv.text = itemView.context.getString(R.string.degree_symbol, temp.eve.toDegree())
                    binding.itemDailyMaxTempTv.text = itemView.context.getString(R.string.degree_symbol, temp.max.toDegree())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        return DailyViewHolder(ItemMainDailyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    interface DayItemClick {
        fun showDetails(data: DailyWeatherModel)
    }
}