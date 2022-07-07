package com.giniapps.weather.data.cache.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.giniapps.weather.data.models.Location
import com.giniapps.weather.data.models.WeatherDetails

@Entity(tableName = "cache")
data class WeatherEntity(
    @PrimaryKey
    val id: Long = 0,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val temperature: Double,
    val summary: String,
    val iconUrl: String
)

fun WeatherEntity.toModel() =
    WeatherDetails(
        location = Location(latitude, longitude, address),
        temperature = temperature,
        summary = summary,
        iconUrl = iconUrl
    )
