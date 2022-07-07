package com.giniapps.weather.data.remote

import android.content.Context
import com.giniapps.weather.data.util.GeocoderUtil
import com.giniapps.weather.data.models.Location
import com.giniapps.weather.data.models.WeatherDetails
import com.giniapps.weather.data.remote.retrofit.Api
import com.giniapps.weather.data.remote.retrofit.response_models.WeatherResponse
import com.giniapps.weather.data.util.NetworkUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource(
    private val context: Context,
    private val scope: CoroutineScope
) : DataSource {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val client = retrofit.create(Api::class.java)

    override suspend fun getWeatherForLocation(lat: Double, lng: Double): WeatherDetails? {
        var result: WeatherDetails? = null
        val job = scope.launch(Dispatchers.IO) {
            if (NetworkUtil.isNetworkAvailable(context) == NetworkUtil.ConnectionStatus.Connected) {
                val response = client
                    .getWeatherFromLocation(Headers, lat, lng)
                    .execute()
                response.body()?.let {
                    result = getWeatherDetailsFromApiResponse(it)
                }
            }
        }
        job.join()
        return result
    }

    private fun getWeatherDetailsFromApiResponse(response: WeatherResponse): WeatherDetails {
        val address = GeocoderUtil.getAddressFromLocation(
            context, response.data.latitude, response.data.longitude
        )
        return WeatherDetails(
            location = Location(
                latitude = response.data.latitude,
                longitude = response.data.longitude,
                address = address
            ),
            temperature = response.data.celsius,
            summary = response.data.summary,
            iconUrl = getIconUrlFromIconName(response.data.icon)
        )
    }

    private fun getIconUrlFromIconName(icon: String) =
        "https://assetambee.s3-us-west-2.amazonaws.com/weatherIcons/PNG/$icon.png"

    companion object {
        private const val ApiKey =
            "3e2eb78ed4bd25f8be137d4b550989672c999e9b80c4f3fd1ef70858cd05ce6c"

        private const val BaseUrl ="https://api.ambeedata.com/"

        private val Headers = mapOf(
            "x-api-key" to ApiKey,
            "Content-type" to "application/json"
        )
    }
}