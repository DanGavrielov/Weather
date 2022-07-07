package com.giniapps.weather.data.cache.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.giniapps.weather.data.cache.room.dao.WeatherDao
import com.giniapps.weather.data.cache.room.entities.WeatherEntity

@Database(
    entities = [WeatherEntity::class], version = 1, exportSchema = false
)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object {
        @Volatile
        private var instance: WeatherDatabase? = null

        fun getDatabase(context: Context): WeatherDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDatabase::class.java,
                    "local_db"
                ).build()
                    .also { instance = it }
            }
        }
    }
}