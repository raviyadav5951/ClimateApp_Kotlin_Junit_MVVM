package com.example.climateforecastapplication.model

/** All model class/data class for holding the response from api call is here **/

data class CurrentLocationWeather(
    var id: Int = 0,
    var base: String? = null,
    var clouds: Clouds? = null,
    var cod: Int = 0,
    var coord: Coord? = null,
    var dt: Long = 0,
    var main: Main? = null,
    var name: String? = null,
    var sys: Sys? = null,
    var timezone: Int = 0,
    var weather: ArrayList<Weather>,
    var wind: Wind? = null
)

data class Wind(
    var deg: Int = 0,
    var speed: Double = 0.0
)


data class Clouds(
    var all: Int = 0
)


data class Main(
    var feels_like: Double = 0.0,
    var grnd_level: Int = 0,
    var humidity: Int = 0,
    var pressure: Int = 0,
    var sea_level: Int = 0,
    var temp: Double = 0.0,
    var temp_max: Double = 0.0,
    var temp_min: Double = 0.0
) 

data class Sys(
    var country: String? = null,
    var sunrise: Long = 0,
    var sunset: Long = 0
) 


data class WeatherResponse(
    var city: City? = null,
    var cod: Int = 0,
    var message: Double = 0.0,
    var cnt: Int = 0,
    var list: ArrayList<WeekList>
) 

data class City(
    var id: Int = 0,
    var name: String? = null,
    var coord: Coord? = null,
    var country: String? = null,
    var population: Int = 0,
    var timezone: Int = 0
) 

data class Coord(
    var lon: Double = 0.0,
    var lat: Double = 0.0
) 

data class WeekList(
    var dt: Long = 0,
    var sunrise: Int = 0,
    var sunset: Int = 0,
    var temp: Temp? = null,
    var feels_like: Feels_like? = null,
    var pressure: Int = 0,
    var humidity: Int = 0,
    var weather: ArrayList<Weather>,
    var speed: Double = 0.0,
    var deg: Int = 0,
    var clouds: Int = 0
) 

data class Temp(
    var day: Double = 0.0,
    var min: Double = 0.0,
    var max: Double = 0.0,
    var night: Double = 0.0,
    var eve: Double = 0.0,
    var morn: Double = 0.0
) 

data class Feels_like(
    var day: Double = 0.0,
    var night: Double = 0.0,
    var eve: Double = 0.0,
    var morn: Double = 0.0
) 

data class Weather(
    var id: Int = 0,
    var main: String? = null,
    var description: String? = null,
    var icon: String? = null
) 


