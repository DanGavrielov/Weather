package com.giniapps.weather.data.cache

import com.giniapps.weather.data.cache.room.dao.WeatherDao
import com.giniapps.weather.data.cache.room.entities.WeatherEntity

class WeatherCache(
    private val weatherDao: WeatherDao
): Cache {
    override suspend fun saveWeatherDetailsToCache(details: WeatherEntity) =
        weatherDao.insertDetails(details)

    override suspend fun getWeatherDetailsFromCache() =
        weatherDao.getDetails()
}