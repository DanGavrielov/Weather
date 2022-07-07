package com.giniapps.weather.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.giniapps.weather.data.util.NetworkUtil
import com.giniapps.weather.ui.InternetConnectionViewModel
import org.koin.java.KoinJavaComponent.inject

class ConnectivityBroadcastReceiver : BroadcastReceiver() {
    private val viewModel: InternetConnectionViewModel by inject(InternetConnectionViewModel::class.java)
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return

        val networkState = NetworkUtil.isNetworkAvailable(context)
        viewModel.notifyUiOnConnectivityChange(networkState)
    }
}