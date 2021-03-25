package com.example.climateforecastapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.climateforecastapplication.model.CurrentLocationWeather
import com.example.climateforecastapplication.model.WeatherResponse
import com.example.climateforecastapplication.retrofit.WeatherApiService

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    val currentLocationData by lazy { MutableLiveData<CurrentLocationWeather>() }
    val fourDaysData by lazy { MutableLiveData<WeatherResponse>() }
    val loadError by lazy { MutableLiveData<Boolean>() }
    val loading by lazy { MutableLiveData<Boolean>() }


    //create disposable and release it later in onCleared
    private val disposable = CompositeDisposable()

    //create api service
    private val api = WeatherApiService()


    fun callWeatherApis(latitude: String?, longitude: String?) {
        loading.value = true
        callCurrentLocationWeatherApi(latitude, longitude)
        callWeatherApiForFourDays(latitude, longitude)

    }

    private fun callCurrentLocationWeatherApi(latitude: String?, longitude: String?) {

        disposable.add(
            api.getCurrentLocation(latitude = latitude, longitude = longitude)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CurrentLocationWeather>() {
                    override fun onSuccess(responseObject: CurrentLocationWeather) {
                        when (responseObject.cod) {
                            200 -> {
                                loadError.value = false
                                loading.value = false
                                currentLocationData.value=responseObject
                                //  Log.e("api response1=", responseObject.toString())
                            }
                            401 -> {
                                loadError.value = true
                                loading.value = false
                                Log.e("api response fail=", responseObject.toString())
                            }
                            else -> {
                                loadError.value = true
                                loading.value = false
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        loadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }

                })
        )
    }

    private fun callWeatherApiForFourDays(latitude: String?, longitude: String?) {

        disposable.add(
            api.getFourDaysForecastData(latitude = latitude, longitude = longitude, countOfDays = 4)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<WeatherResponse>() {
                    override fun onSuccess(responseObject: WeatherResponse) {
                        when (responseObject.cod) {
                            200 -> {
                                fourDaysData.value=responseObject
                                loadError.value = false
                                loading.value = false
                                //  Log.e("api response2=", responseObject.toString())
                            }
                            401 -> {
                                loadError.value = true
                                loading.value = false
                                Log.e("api response fail=", responseObject.toString())
                            }
                            else -> {
                                loadError.value = true
                                loading.value = false
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        loadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }

                })
        )
    }




    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}