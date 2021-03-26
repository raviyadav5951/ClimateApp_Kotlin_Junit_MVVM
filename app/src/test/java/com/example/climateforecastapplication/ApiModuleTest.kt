package com.example.climateforecastapplication


import com.example.climateforecastapplication.di.ApiModule
import com.example.climateforecastapplication.retrofit.WeatherApiService

class ApiModuleTest (val mockApiModule:WeatherApiService) : ApiModule(){
    override fun provideWeatherApiService(): WeatherApiService {
        return mockApiModule
    }

}