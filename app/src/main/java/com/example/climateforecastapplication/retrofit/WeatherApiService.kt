package com.example.climateforecastapplication.retrofit

import com.example.climateforecastapplication.BuildConfig
import com.example.climateforecastapplication.BuildConfig.API_KEY
import com.example.climateforecastapplication.di.DaggerApiComponent
import com.example.climateforecastapplication.model.CurrentLocationWeather
import com.example.climateforecastapplication.model.WeatherResponse
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * This class can also be termed as Repository in MVVM terminology
 */
//{"city":{"id":1259229,"name":"Pune","coord":{"lon":73.8553,"lat":18.5196},"country":"IN","population":9999,"timezone":19800},"cod":"200","message":0.0579592,"cnt":4,"list":[{"dt":1616653800,"sunrise":1616634274,"sunset":1616678186,"temp":{"day":34.26,"min":25.96,"max":36,"night":28.11,"eve":35.49,"morn":25.96},"feels_like":{"day":28.91,"night":24.03,"eve":32.05,"morn":24.83},"pressure":1013,"humidity":14,"weather":[{"id":802,"main":"Clouds","description":"scattered clouds","icon":"03d"}],"speed":5.48,"deg":60,"clouds":34,"pop":0},{"dt":1616740200,"sunrise":1616720624,"sunset":1616764599,"temp":{"day":35.4,"min":24.82,"max":37.66,"night":29.06,"eve":36.96,"morn":25.2},"feels_like":{"day":30.17,"night":25.17,"eve":33.14,"morn":21.78},"pressure":1012,"humidity":10,"weather":[{"id":800,"main":"Clear","description":"sky is clear","icon":"01d"}],"speed":4.46,"deg":76,"clouds":0,"pop":0},{"dt":1616826600,"sunrise":1616806973,"sunset":1616851012,"temp":{"day":36.22,"min":24.98,"max":37.98,"night":28.77,"eve":37.64,"morn":24.98},"feels_like":{"day":29.84,"night":25.3,"eve":33.08,"morn":21.45},"pressure":1011,"humidity":7,"weather":[{"id":800,"main":"Clear","description":"sky is clear","icon":"01d"}],"speed":5.37,"deg":59,"clouds":4,"pop":0},{"dt":1616913000,"sunrise":1616893323,"sunset":1616937424,"temp":{"day":36.93,"min":25.4,"max":39.11,"night":26.7,"eve":36.72,"morn":25.4},"feels_like":{"day":32.46,"night":26.31,"eve":33.43,"morn":22.44},"pressure":1009,"humidity":8,"weather":[{"id":802,"main":"Clouds","description":"scattered clouds","icon":"03d"}],"speed":3.02,"deg":50,"clouds":40,"pop":0}]}
//http://api.openweathermap.org/data/2.5/forecast/daily?q=Pune&units=metric&cnt=4&appid=cc65d87afabccabcd3c47633ef7d504d

class WeatherApiService {

    init {
        DaggerApiComponent.create().inject(this)
    }

    //Injecting the dependency
    @Inject
    lateinit var apiInterface:WeatherApiInterface

    companion object{
        val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        val COUNT_OF_DAYS=5
    }


    //These fun will call  api for fetching current location weather data
    fun getCurrentLocation(latitude: String?, longitude: String?): Single<CurrentLocationWeather> =
        apiInterface.getCurrentLocationWeather(latitude, longitude, appid = API_KEY)


    //fetch 4 days forecast data
    open fun getFourDaysForecastData(
        latitude: String?,
        longitude: String?,
        countOfDays: Int
    ): Single<WeatherResponse> = apiInterface.getFourDaysForecastData(
        latitude = latitude,
        longitude = longitude,
        countOfDays = countOfDays,
        appid = API_KEY
    )
}