package com.giniapps.weather.ui.map_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giniapps.weather.data.models.WeatherDetails
import com.giniapps.weather.data.repository.MainRepository
import com.giniapps.weather.data.repository.Repository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.plus

class MapScreenViewModel(
    private val repository: Repository
): ViewModel() {
    private val _weatherDetailsState = MutableStateFlow(WeatherDetails.emptyObject())
    val weatherDetailsState get() = _weatherDetailsState.asStateFlow()
    private var job = Job()

    fun getWeatherDetailsForLocation(lat: Double, lng: Double) {
        resetJob()
        flow<WeatherDetails> {
            while (true) {
                val details = repository.getWeatherDetailsForLocation(lat, lng)
                _weatherDetailsState.emit(details)
                delay(10_000)
            }
        }.launchIn(viewModelScope + job)
    }

    private fun resetJob() {
        job.cancel()
        job = Job()
    }
}