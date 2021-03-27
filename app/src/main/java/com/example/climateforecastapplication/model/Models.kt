package com.example.climateforecastapplication.model

/** All model class/data class for holding the response from api call is here **/

data class CurrentLocationWeather(
    val id: Int = 0,
    val cod: Int = 0,
    val main: Main? = null,
    val name: String? = null,
)

data class Main(
    val temp: Double = 0.0
) 


data class WeatherResponse(
    val city: City? = null,
    val cod: Int = 0,
    val message: Double = 0.0,
    val cnt: Int = 0,
    val list: ArrayList<WeekList>
) 

data class City(
    val id: Int = 0,
    val name: String? = null
) 

data class WeekList(
    val dt: Long = 0,
    val temp: Temp? = null

) 

data class Temp(
    val day: Double = 0.0

) 



