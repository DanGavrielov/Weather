package com.giniapps.weather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giniapps.weather.data.util.NetworkUtil
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class InternetConnectionViewModel : ViewModel() {
    val connectionState = MutableSharedFlow<NetworkUtil.ConnectionStatus>()

    fun notifyUiOnConnectivityChange(
        connectionStatus: NetworkUtil.ConnectionStatus
    ) = viewModelScope.launch {
        connectionState.emit(connectionStatus)
    }
}