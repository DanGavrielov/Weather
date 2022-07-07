package com.giniapps.weather.data.location

import android.location.Location

interface LocationListener {
    fun handleLocationUpdate(location: Location)
}