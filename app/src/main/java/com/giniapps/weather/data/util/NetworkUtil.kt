package com.giniapps.weather.data.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object NetworkUtil {
    fun isNetworkAvailable(context: Context): ConnectionStatus {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return ConnectionStatus.NotConnected
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return ConnectionStatus.NotConnected
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> ConnectionStatus.Connected
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> ConnectionStatus.Connected
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> ConnectionStatus.Connected
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> ConnectionStatus.Connected
                else -> ConnectionStatus.NotConnected
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return ConnectionStatus.NotConnected
            return if (nwInfo.isConnected) ConnectionStatus.Connected else ConnectionStatus.NotConnected
        }
    }

    enum class ConnectionStatus {
        Connected, NotConnected
    }
}