package com.giniapps.weather.data.remote

import com.giniapps.weather.data.models.WeatherDetails

interface DataSource {
    suspend fun getWeatherForLocation(lat: Double, lng: Double): WeatherDetails?
}