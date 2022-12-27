package com.example.weatherapp

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.business.model.DailyWeatherModel
import com.example.weatherapp.databinding.FragmentDailyListBinding
import com.example.weatherapp.view.adapters.MainDailyAdapter
// TODO доделать DailyListFragment, MainDailyAdapter, создать DailyInfoDeteilsFragment
const val TAGF = "DAILY LIST FRAGMENT"
class DailyListFragment : DailyBaseFragment<List<DailyWeatherModel>>() {

    private var _binding: FragmentDailyListBinding? = null
    private val binding get() = _binding!!

    private val dailyAdapter = MainDailyAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.slide_out_rigth)
        enterTransition = inflater.inflateTransition(R.transition.slide_out_rigth)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDailyListBinding.inflate(inflater, container, false)
        Log.d(TAGF, "onCreateView: +")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAGF, "onViewCreated: +")
        binding.dallyList.apply {
            adapter = dailyAdapter
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            dailyAdapter.clickListener = clickListener
        }
    }

    override fun updateView() {
        dailyAdapter.updateData(mData!!)
    }

    private val clickListener = object: MainDailyAdapter.DayItemClick {
        override fun showDetails(data: DailyWeatherModel) {
            val fragment = DailyInfoFragment()
            fragment.setData(data)
            fm.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit()
        }

    }
}