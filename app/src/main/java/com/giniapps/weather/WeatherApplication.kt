package com.giniapps.weather

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.giniapps.weather.data.di.cacheModule
import com.giniapps.weather.data.di.remoteModule
import com.giniapps.weather.data.di.repositoryModule
import com.giniapps.weather.data.di.viewModelModule
import com.giniapps.weather.workers.UpdateCurrentWeatherWorker
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.time.Duration

class WeatherApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@WeatherApplication)
            modules(
                remoteModule,
                cacheModule,
                repositoryModule,
                viewModelModule
            )
        }

        scheduleWorker()
    }

    private fun scheduleWorker() {
        val updateLocalDataRequest = PeriodicWorkRequestBuilder<UpdateCurrentWeatherWorker>(
            Duration.ofDays(1)
        ).build()

        WorkManager
            .getInstance(this)
            .enqueueUniquePeriodicWork(
                "update_worker",
                ExistingPeriodicWorkPolicy.KEEP,
                updateLocalDataRequest
            )
    }
}