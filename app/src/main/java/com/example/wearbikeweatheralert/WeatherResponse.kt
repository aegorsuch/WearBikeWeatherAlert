package com.example.wearbikeweatheralert

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val hourly: List<HourlyWeather>
)

data class HourlyWeather(
    val dt: Long,
    val temp: Double,
    val weather: List<WeatherDescription>,
    val pop: Double // Probability of precipitation
)

data class WeatherDescription(
    val main: String,
    val description: String,
    val icon: String
)