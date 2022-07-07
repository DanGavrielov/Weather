package com.giniapps.weather.data.models

import com.giniapps.weather.data.cache.room.entities.WeatherEntity

data class WeatherDetails(
    val location: Location,
    val temperature: Double,
    val summary: String,
    val iconUrl: String
) {
    companion object {
        fun emptyObject() =
            WeatherDetails(
                location = Location(0.0, 0.0, ""),
                temperature = 0.0,
                summary = "",
                iconUrl = ""
            )
    }
}

data class Location(
    val latitude: Double,
    val longitude: Double,
    val address: String
)

fun WeatherDetails.toEntity() =
    WeatherEntity(
        latitude = location.latitude,
        longitude = location.longitude,
        address = location.address,
        temperature = temperature,
        summary = summary,
        iconUrl = iconUrl
    )

fun WeatherDetails.isNotEmpty() =
    this != WeatherDetails.emptyObject()

fun WeatherDetails.isEmpty() =
    this == WeatherDetails.emptyObject()