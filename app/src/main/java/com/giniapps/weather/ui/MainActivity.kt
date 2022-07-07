package com.giniapps.weather.ui

import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.giniapps.weather.R
import com.giniapps.weather.data.util.NetworkUtil
import com.giniapps.weather.databinding.ActivityMainBinding
import com.giniapps.weather.receivers.ConnectivityBroadcastReceiver
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: InternetConnectionViewModel by inject()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var receiver: ConnectivityBroadcastReceiver
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        registerInternetConnectivityBroadcastReceiver()
        listenToConnectivityStateChanges()
    }

    private fun listenToConnectivityStateChanges() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.connectionState.collectLatest { status ->
                        when (status) {
                            NetworkUtil.ConnectionStatus.Connected -> {
                                snackbar?.dismiss()
                            }
                            NetworkUtil.ConnectionStatus.NotConnected -> {
                                snackbar = Snackbar.make(
                                    binding.root,
                                    R.string.no_internet,
                                    Snackbar.LENGTH_INDEFINITE
                                ).also { it.show() }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun registerInternetConnectivityBroadcastReceiver() {
        receiver = ConnectivityBroadcastReceiver()
        val intentFilter = IntentFilter().also {
            it.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        }

        registerReceiver(receiver, intentFilter)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(receiver)
    }
}