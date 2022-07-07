package com.giniapps.weather.data.cache

import com.giniapps.weather.data.cache.room.entities.WeatherEntity

interface Cache {
    suspend fun saveWeatherDetailsToCache(details: WeatherEntity)
    suspend fun getWeatherDetailsFromCache(): WeatherEntity?
}