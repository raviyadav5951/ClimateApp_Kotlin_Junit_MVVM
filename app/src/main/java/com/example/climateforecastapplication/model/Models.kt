package com.example.climateforecastapplication.model

/** All model class/data class for holding the response from api call is here **/

data class CurrentLocationWeather(
    val id: Int = 0,
    val base: String? = null,
    val clouds: Clouds? = null,
    val cod: Int = 0,
    val coord: Coord? = null,
    val dt: Long = 0,
    val main: Main? = null,
    val name: String? = null,
    val sys: Sys? = null,
    val timezone: Int = 0,
    val weather: ArrayList<Weather>,
    val wind: Wind? = null
)

data class Wind(
    val deg: Int = 0,
    val speed: Double = 0.0
)


data class Clouds(
    val all: Int = 0
)


data class Main(
    val temp: Double = 0.0,
) 

data class Sys(
    val country: String? = null,
    val sunrise: Long = 0,
    val sunset: Long = 0
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

data class Coord(
    val lon: Double = 0.0,
    val lat: Double = 0.0
) 

data class WeekList(
    val dt: Long = 0,
    val temp: Temp? = null,

) 

data class Temp(
    val day: Double = 0.0,

) 

data class Feels_like(
    val day: Double = 0.0,
    val night: Double = 0.0,
    val eve: Double = 0.0,
    val morn: Double = 0.0
) 

data class Weather(
    val id: Int = 0,
    val main: String? = null,
    val description: String? = null,
    val icon: String? = null
) 


