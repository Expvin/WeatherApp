package com.example.weatherapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.weatherapp.R
import com.example.weatherapp.business.model.GeoCodeModel
import com.example.weatherapp.databinding.ItemCityListBinding
import com.google.android.material.button.MaterialButton
import java.util.*

class CityListAdapter: BaseAdapter<GeoCodeModel>() {

    lateinit var clickListener: SearchItemClickListener

    inner class CityListHolder(val binding: ItemCityListBinding): BaseViewHolder(binding.root) {
        override fun bindView(position: Int) {

            binding.location.setOnClickListener {
                clickListener.showWeatherIn(mData[position])
            }
            binding.favorite.setOnClickListener {
                val item = mData[position]
                when ((it as MaterialButton).isChecked) {
                    true -> {
                        item.isFavorite = true
                        clickListener.addToFavorite(item) }
                    false -> {
                        item.isFavorite = false
                        clickListener.removeFromFavorite(item)
                    }
                }
            }

            mData[position].apply {
                binding.state.text = if (!state.isNullOrEmpty()) itemView.context.getString(R.string.comma, state) else ""
//                binding.searchCity.text = when (Locale.getDefault().displayLanguage) {
//                    "русский" -> local_names.ru?: name
//                    "English" -> local_names.en?: name
//                    else -> "non" }
                binding.searchCity.text = name
                binding.country.text = Locale("", country).displayName
                binding.favorite.isChecked = isFavorite
            }

        }

    }

    interface SearchItemClickListener {

        fun addToFavorite(item: GeoCodeModel)

        fun removeFromFavorite(item: GeoCodeModel)

        fun showWeatherIn(item: GeoCodeModel)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityListHolder {
        return CityListHolder(ItemCityListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
}