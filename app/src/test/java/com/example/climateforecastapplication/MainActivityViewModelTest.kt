package com.example.climateforecastapplication

import android.app.Application
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule


import com.example.climateforecastapplication.di.AppModule
import com.example.climateforecastapplication.di.DaggerViewModelComponent
import com.example.climateforecastapplication.model.City
import com.example.climateforecastapplication.model.Temp
import com.example.climateforecastapplication.model.WeatherResponse
import com.example.climateforecastapplication.model.WeekList
import com.example.climateforecastapplication.retrofit.WeatherApiService
import com.example.climateforecastapplication.viewmodel.MainActivityViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainActivityViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()


    @Mock
    lateinit var animalService: WeatherApiService

    val application = mock(Application::class.java)

    var listViewModel = MainActivityViewModel(application)


    @Before
    fun setUpRxSchedulers() {
        // instrumentationContext = InstrumentationRegistry.getInstrumentation().context
        val immediate = object : Scheduler() {
            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker({ it.run() }, true)
            }
        }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }
    }


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        DaggerViewModelComponent.builder()
            .appModule(AppModule(application))
            .apiModule(ApiModuleTest(animalService))
            .build()
            .inject(listViewModel)

        // listViewModel=MainActivityViewModel(application)
    }

    @Test
    fun getWeatherListSuccess() {
        val latitude = "22"
        val longitude = "72"

        val city = City(1, "Pune")
        val weekObject = WeekList(1, Temp(day = 35.0))
        val weekList = arrayListOf(weekObject)

        val weatherResponse = WeatherResponse(city = city, 200, 0.0, 0, weekList)

        val testSingle = Single.just(weatherResponse)

        Mockito.`when`(animalService.getFourDaysForecastData(latitude, longitude, 1))
            .thenReturn(testSingle)


        listViewModel.callWeatherApis(latitude, longitude,1)

        Assert.assertEquals(1, listViewModel.fourDaysData.value?.list?.size)
        Assert.assertEquals(false, listViewModel.loadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)
    }

    @Test
    fun getWeatherListFail() {
        val latitude = "22"
        val longitude = "72"


        Mockito.`when`(animalService.getFourDaysForecastData(latitude, longitude, 0))
            .thenReturn(null)


        listViewModel.callWeatherApis(latitude, longitude,0)

        Assert.assertEquals(null, listViewModel.fourDaysData.value?.list?.size)
        Assert.assertEquals(true, listViewModel.loadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)
    }



}