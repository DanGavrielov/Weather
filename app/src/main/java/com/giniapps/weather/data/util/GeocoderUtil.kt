package com.giniapps.weather.data.util

import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.io.IOException
import java.util.*

object GeocoderUtil {
    fun getAddressFromLocation(context: Context, lat: Double, lng: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>?
        return try {
            addresses = geocoder.getFromLocation(lat, lng, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val locality = addresses[0].locality ?: ""
                "${addresses[0].countryName}${if (locality.isNotEmpty()) ", $locality" else ""}"
            } else "N/A"
        } catch (ignored: IOException) {
            "N/A"
        }
    }
}