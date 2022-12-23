package com.example.weatherapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.business.model.GeoCodeModel
import com.example.weatherapp.databinding.ActivityMenuBinding
import com.example.weatherapp.presenters.MenuPresenter
import com.example.weatherapp.view.MenuView
import com.example.weatherapp.view.adapters.CityListAdapter
import com.example.weatherapp.view.createObservable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_menu.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import java.util.concurrent.TimeUnit



class MenuActivity : MvpAppCompatActivity(), MenuView {
    private val presenter by moxyPresenter { MenuPresenter() }
    lateinit var binding: ActivityMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter.enable()
        presenter.getFavoriteList()

        initCityList(binding.predictions)
        initCityList(binding.favorites)

        search_field.createObservable()
            .doOnNext { setLoading(true) }
            .debounce(700, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                //if (it.isNotEmpty()) presenter.search_for(it)
            }

    }

    private fun initCityList(rv: RecyclerView) {
        val cityAdapter = CityListAdapter()
        cityAdapter.clickListener = searchItemClickListener
        rv.apply {
            adapter = cityAdapter
            layoutManager = object: LinearLayoutManager(this@MenuActivity, VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            setHasFixedSize(true)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_left)
    }

    override fun setLoading(flag: Boolean) {
        binding.loading.isActivated = flag
        binding.loading.visibility = if (flag) View.VISIBLE else View.GONE
    }

    override fun fillPredictionList(data: List<GeoCodeModel>) {
        (binding.predictions.adapter as CityListAdapter).updateData(data)
    }

    override fun fillFavoriteList(data: List<GeoCodeModel>) {
        (binding.favorites.adapter as CityListAdapter).updateData(data)
    }

    private val searchItemClickListener = object : CityListAdapter.SearchItemClickListener {
        override fun addToFavorite(item: GeoCodeModel) {
            presenter.saveLocation(item)
        }

        override fun removeFromFavorite(item: GeoCodeModel) {
            presenter.removeLocation(item)
        }

        override fun showWeatherIn(item: GeoCodeModel) {
            val intent = Intent(this@MenuActivity, MainActivity::class.java)
            val bundle = Bundle()
            bundle.putString("lat", item.lat.toString())
            bundle.putString("lon", item.lon.toString())
            intent.putExtra(COORDINATES, bundle)
            overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_left)
        }

    }

}