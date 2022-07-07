package com.giniapps.weather.data.repository

import com.giniapps.weather.data.cache.Cache
import com.giniapps.weather.data.cache.room.entities.toModel
import com.giniapps.weather.data.models.WeatherDetails
import com.giniapps.weather.data.models.toEntity
import com.giniapps.weather.data.remote.DataSource

class MainRepository(
    override val dataSource: DataSource,
    override val cache: Cache
) : Repository {
    override suspend fun updateCurrentLocation(lat: Double, lng: Double) {
        val details = dataSource.getWeatherForLocation(lat, lng)
        details?.let {
            cache.saveWeatherDetailsToCache(it.toEntity())
        }
    }

    override suspend fun getWeatherDetailsForCurrentLocation(): WeatherDetails {
        val cachedDetails = cache.getWeatherDetailsFromCache()
        return cachedDetails?.toModel() ?: WeatherDetails.emptyObject()
    }

    override suspend fun getWeatherDetailsForLocation(lat: Double, lng: Double): WeatherDetails {
        val details = dataSource.getWeatherForLocation(lat, lng)
        return details ?: WeatherDetails.emptyObject()
    }
}