package com.giniapps.weather.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.giniapps.weather.data.models.toEntity
import com.giniapps.weather.data.repository.Repository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

class UpdateCurrentWeatherWorker(
    private val appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {
    private val repository by KoinJavaComponent.inject<Repository>(Repository::class.java)
    private val scope by KoinJavaComponent.inject<CoroutineScope>(CoroutineScope::class.java)
    override fun doWork(): Result {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(appContext)
        val priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY
        val cancellationTokenSource = CancellationTokenSource()
        fusedLocationClient.getCurrentLocation(priority, cancellationTokenSource.token)
            .addOnSuccessListener { location ->
                scope.launch {
                    val details = repository.dataSource
                        .getWeatherForLocation(location.latitude, location.longitude)
                    details?.let {
                        repository.cache.saveWeatherDetailsToCache(it.toEntity())
                    }
                }
            }
        return Result.success()
    }
}