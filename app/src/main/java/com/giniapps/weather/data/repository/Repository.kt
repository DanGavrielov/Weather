package com.giniapps.weather.data.repository

import com.giniapps.weather.data.cache.Cache
import com.giniapps.weather.data.models.WeatherDetails
import com.giniapps.weather.data.remote.DataSource

interface Repository {
    val dataSource: DataSource
    val cache: Cache

    suspend fun updateCurrentLocation(lat: Double, lng: Double)
    suspend fun getWeatherDetailsForCurrentLocation(): WeatherDetails

    suspend fun getWeatherDetailsForLocation(lat: Double, lng: Double): WeatherDetails
}