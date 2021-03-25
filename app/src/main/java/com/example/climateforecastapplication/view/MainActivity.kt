package com.example.climateforecastapplication.view


import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.climateforecastapplication.databinding.ActivityMainBinding
import com.example.climateforecastapplication.model.CurrentLocationWeather
import com.example.climateforecastapplication.model.WeatherResponse
import com.example.climateforecastapplication.retrofit.Utils
import com.example.climateforecastapplication.viewmodel.MainActivityViewModel
import mumayank.com.airlocationlibrary.AirLocation


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var airLocation: AirLocation
    private lateinit var viewModel: MainActivityViewModel

    private var weatherListAdapter=WeatherListAdapter(arrayListOf())
   // private var list: ArrayList<WeekList>? = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        //creating viewmodel
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        setUpRecyclerView()

        //observer assignment
        viewModel.currentLocationData.observe(this, currentLocationDataObserver)
        viewModel.fourDaysData.observe(this, fourDayClimateDataObserver)
        viewModel.loading.observe(this, loadingLiveDataObserver)
        viewModel.loadError.observe(this, loadingErrorDataObserver)

        if(!Utils.isNetworkAvailable(this)){
            Toast.makeText(this,"You are offline.Please switch on the internet connection",Toast.LENGTH_LONG).show()
        }

        setupLocation()
    }


    private fun setUpRecyclerView() {
        binding.listClimate.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.listClimate.adapter=weatherListAdapter

    }

    private val loadingErrorDataObserver = androidx.lifecycle.Observer<Boolean> { isError ->
        binding.listError.visibility = if (isError) View.VISIBLE else View.GONE
    }

    private val loadingLiveDataObserver = androidx.lifecycle.Observer<Boolean> { isLoading ->
        binding.loadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading) {
            binding.listError.visibility = View.GONE

        }
    }

    private val currentLocationDataObserver =
        androidx.lifecycle.Observer<CurrentLocationWeather> { currentLocationData ->
            currentLocationData?.let {
                Log.e("main","current day observer")
                binding.layoutContainer.visibility=View.VISIBLE
                binding.tvCurrentCity.text=currentLocationData.name
                binding.tvCurrentTemperature.text=currentLocationData.main?.temp?.toInt().toString() + "°C"
            }
        }

    private val fourDayClimateDataObserver =
        androidx.lifecycle.Observer<WeatherResponse> { fourDaysData ->
            fourDaysData?.let {
                Log.e("main","four day observer")
                populateWeatherListData(fourDaysData)
            }
        }


    private fun setupLocation() {

        airLocation = AirLocation(this, shouldWeRequestPermissions = true,shouldWeRequestOptimization = true, object : AirLocation.Callbacks {
            override fun onSuccess(location: Location) {
                // location fetched successfully, proceed with it

//                Log.e("loc", "lat=${location.latitude}")
//                Log.e("loc", "long=${location.longitude}")

                viewModel.callWeatherApis(
                    latitude = location.latitude.toString(),
                    longitude = location.longitude.toString()
                )

            }

            override fun onFailed(locationFailedEnum: AirLocation.LocationFailedEnum) {
                // couldn't fetch location due to reason available in locationFailedEnum
                // you may optionally do something to inform the user, even though the reason may be obvious

                Toast.makeText(applicationContext,"Location permission is required to proceed ahead in the application.",Toast.LENGTH_LONG).show()

            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        airLocation.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        airLocation.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun populateWeatherListData(fourDaysData: WeatherResponse) {
        try {
            //Log.e("main","in populateData")
            fourDaysData.let{
                binding.layoutContainer.visibility=View.VISIBLE
                binding.listClimate.visibility=View.VISIBLE
                weatherListAdapter.updateWeatherList(fourDaysData.list)
            }
            binding.loadingView.visibility = View.GONE
            if (fourDaysData.list.isNullOrEmpty()) {
                binding.listError.visibility = View.VISIBLE
                binding.listClimate.visibility = View.GONE
            }
        }
        catch (e: Exception) {
            binding.loadingView.visibility = View.GONE
            binding.listError.visibility = View.VISIBLE
            e.printStackTrace()
        }

    }



}