package com.giniapps.weather.ui.main_screen

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giniapps.weather.data.location.LocationListener
import com.giniapps.weather.data.location.LocationManager
import com.giniapps.weather.data.models.WeatherDetails
import com.giniapps.weather.data.repository.Repository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val repository: Repository,
    private val locationManager: LocationManager
): ViewModel() {
    private val _weatherDetailsState = MutableStateFlow(WeatherDetails.emptyObject())
    val weatherDetailsState get() = _weatherDetailsState.asStateFlow()

    init {
        setupLocationListener()
        flow<WeatherDetails> {
            while (true) {
                val details = repository.getWeatherDetailsForCurrentLocation()
                _weatherDetailsState.emit(details)
                delay(10_000)
            }
        }.launchIn(viewModelScope)
    }

    private fun setupLocationListener() {
        locationManager.listener = object : LocationListener {
            override fun handleLocationUpdate(location: Location) {
                viewModelScope.launch {
                    repository.updateCurrentLocation(location.latitude, location.longitude)
                }
            }
        }
        locationManager.requestLocationUpdates()
    }
}