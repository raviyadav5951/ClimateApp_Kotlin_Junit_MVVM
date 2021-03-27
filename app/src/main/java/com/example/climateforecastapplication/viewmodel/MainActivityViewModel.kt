package com.example.climateforecastapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.climateforecastapplication.di.AppModule
import com.example.climateforecastapplication.di.DaggerViewModelComponent
import com.example.climateforecastapplication.model.CurrentLocationWeather
import com.example.climateforecastapplication.model.WeatherResponse
import com.example.climateforecastapplication.retrofit.Utils
import com.example.climateforecastapplication.retrofit.WeatherApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 *  We have taken injected var to know if viewmodel called from reallife scenario or from test class.
 *  If ListViewModel called from test then injected=true and we don't inject DaggerComponent since its a test and we have mock.
 */

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    val currentLocationData by lazy { MutableLiveData<CurrentLocationWeather>() }
    val fourDaysData by lazy { MutableLiveData<WeatherResponse>() }
    val loadError by lazy { MutableLiveData<Boolean>() }
    val loading by lazy { MutableLiveData<Boolean>() }

    constructor(application: Application,test:Boolean=true):this(application){
        injected=true
    }

    private var injected=false

    //create disposable and release it later in onCleared
    private val disposable = CompositeDisposable()


    //create api service (Injecting the dependency)
    @Inject
    lateinit var api: WeatherApiService

//    init {
//        DaggerViewModelComponent.create().inject(this)
//    }


    fun inject(){
        if(!injected){
            DaggerViewModelComponent.builder()
                .appModule(AppModule(getApplication()))
                .build()
                .inject(this)
        }
    }

    /**
     * Method container to call the weather apis.
     */
     fun callWeatherApis(latitude: String?, longitude: String?,countOfDays: Int) {
        inject()
        loading.postValue(true)
        callCurrentLocationWeatherApi(latitude, longitude)
        callWeatherApiForFourDays(latitude, longitude,countOfDays)

    }

    /**
     * To handle the error state on no internet connection
     *
     */
    fun checkConnectionAndCallApis(latitude: String?, longitude: String?,countOfDays: Int){
        if(Utils.isNetworkAvailable(getApplication())){

            loadError.postValue(false)
            callWeatherApis(latitude,longitude,countOfDays)
        }
        else{
            loadError.postValue(true)
            loading.postValue(false)
        }
    }

    /**
     * This api will return the weather data for the current location
     * based on latitude and longitude
     */
     fun callCurrentLocationWeatherApi(latitude: String?, longitude: String?) {

        disposable.add(
            api.getCurrentLocation(latitude = latitude, longitude = longitude)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CurrentLocationWeather>() {
                    override fun onSuccess(responseObject: CurrentLocationWeather) {
                        when (responseObject.cod) {
                            200 -> {
                                loadError.postValue(false)
                                loading.postValue(false)
                                currentLocationData.postValue(responseObject)
                                //  Log.e("api response1=", responseObject.toString())
                            }
                            401 -> {
                                loadError.postValue( true)
                                loading.postValue(false)
                               // Log.e("api response fail=", responseObject.toString())
                            }
                            else -> {
                                loadError.postValue( true)
                                loading.postValue(false)
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        loadError.postValue( true)
                        loading.postValue(false)
                        e.printStackTrace()
                    }

                })
        )
    }

    /**
     * This api will return the weather forecast list for data for the current location
     * based on latitude and longitude.
     * We have to mention countOfDays to get the forecast.
     */
     fun callWeatherApiForFourDays(latitude: String?, longitude: String?,countOfDays:Int) {

        disposable.add(
            api.getFourDaysForecastData(latitude = latitude, longitude = longitude, countOfDays = countOfDays)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<WeatherResponse>() {
                    override fun onSuccess(responseObject: WeatherResponse) {
                        when (responseObject.cod) {
                            200 -> {
                                fourDaysData.postValue(responseObject)
                                loadError.postValue( false)
                                loading.postValue(false)
                                //  Log.e("api response2=", responseObject.toString())
                            }
                            401 -> {
                                loadError.postValue(true)
                                loading.postValue(false)
                               // Log.e("api response fail=", responseObject.toString())
                            }
                            else -> {
                                loadError.postValue(true)
                                loading.postValue(false)
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        loadError.postValue( true)
                        loading.postValue(false)
                        fourDaysData.postValue(null)
                        e.printStackTrace()
                    }

                })
        )
    }


    /**
     * Clearing the disposable once its task is over.
     */
    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}