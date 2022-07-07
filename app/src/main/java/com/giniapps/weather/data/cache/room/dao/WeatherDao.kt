package com.giniapps.weather.data.cache.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.giniapps.weather.data.cache.room.entities.WeatherEntity

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetails(weatherDetails: WeatherEntity)

    @Query("SELECT * FROM cache LIMIT 1")
    suspend fun getDetails(): WeatherEntity
}