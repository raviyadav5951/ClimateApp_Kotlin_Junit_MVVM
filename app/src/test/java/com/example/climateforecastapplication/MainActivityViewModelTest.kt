package com.example.climateforecastapplication

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.climateforecastapplication.di.AppModule
import com.example.climateforecastapplication.di.DaggerViewModelComponent
import com.example.climateforecastapplication.model.*
import com.example.climateforecastapplication.retrofit.WeatherApiService
import com.example.climateforecastapplication.viewmodel.MainActivityViewModel
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations


class MainActivityViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()


    @Mock
    lateinit var weatherApiService: WeatherApiService

    @Mock
    val application = mock(Application::class.java)

     var listViewModel =MainActivityViewModel(application,true)


    @Before
    fun setUpRxSchedulers() {

        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

    }


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        DaggerViewModelComponent.builder()
            .appModule(AppModule(application))
            .apiModule(ApiModuleTest(weatherApiService))
            .build()
            .inject(listViewModel)
    }

    @Test
    fun getForecastWeatherListSuccess() {
        //creating stub
        val latitude = "22"
        val longitude = "72"

        val city = City(1, "Pune")
        val weekObject = WeekList(1, Temp(day = 35.0))
        val weekList = arrayListOf(weekObject)

        val testSingle = Single.just(WeatherResponse(city,200,0.0,0,weekList))
        Mockito.`when`(weatherApiService.getFourDaysForecastData(latitude, longitude, 1)).thenReturn(testSingle)

        listViewModel.callWeatherApiForFourDays(latitude = latitude,longitude =  longitude,countOfDays = 1)

        Assert.assertEquals(1, listViewModel.fourDaysData.value?.list?.size)
        Assert.assertEquals(false, listViewModel.loadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)
    }

    @Test
    fun getForecastWeatherListFail() {

        //creating stub
        val testSingle=Single.error<WeatherResponse>(Throwable())

        val latitude = "22"
        val longitude = "72"

        Mockito.`when`(weatherApiService.getFourDaysForecastData(latitude, longitude, 1)).thenReturn(testSingle)

        listViewModel.callWeatherApiForFourDays(latitude = latitude,longitude =  longitude,countOfDays = 1)

        Assert.assertEquals(null, listViewModel.fourDaysData.value?.list?.size)
        Assert.assertEquals(true, listViewModel.loadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)
    }



    @Test
    fun getCurrentLocationWeatherSuccess() {
        //creating stub
        val latitude = "22"
        val longitude = "72"
        val currentLocationWeather=CurrentLocationWeather(1,200,Main(temp = 35.0),name = "Pune")

        val testSingle = Single.just(currentLocationWeather)

        Mockito.`when`(weatherApiService.getCurrentLocation(latitude, longitude)).thenReturn(testSingle)

        listViewModel.callCurrentLocationWeatherApi(latitude = latitude,longitude =  longitude)

        Assert.assertEquals(35.0, listViewModel.currentLocationData.value?.main?.temp)
        Assert.assertEquals("Pune", listViewModel.currentLocationData.value?.name)
        Assert.assertEquals(false, listViewModel.loadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)
    }

    @Test
    fun getCurrentLocationWeatherFail() {
        //creating stub
        val latitude = "22"
        val longitude = "72"
        val currentLocationWeather=CurrentLocationWeather(1,401,Main(temp = 0.0),name = "Pune")

        val testSingle = Single.just(currentLocationWeather)

        Mockito.`when`(weatherApiService.getCurrentLocation(latitude, longitude)).thenReturn(testSingle)

        listViewModel.callCurrentLocationWeatherApi(latitude = latitude,longitude =  longitude)


        Assert.assertEquals(true, listViewModel.loadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)
    }



}