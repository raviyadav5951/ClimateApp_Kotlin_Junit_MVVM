package com.example.climateforecastapplication.di

import com.example.climateforecastapplication.retrofit.WeatherApiService
import dagger.Component


@Component(modules = [ApiModule::class])
interface ApiComponent {

    //WeatherApiService determines where this will be injected
    fun inject(service:WeatherApiService)
}