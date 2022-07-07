package com.giniapps.weather.ui.map_screen

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.giniapps.weather.R
import com.giniapps.weather.data.models.isEmpty
import com.giniapps.weather.data.models.isNotEmpty
import com.giniapps.weather.data.util.NetworkUtil
import com.giniapps.weather.ui.main_screen.MainScreenViewModel
import com.giniapps.weather.databinding.FragmentMapBinding
import com.giniapps.weather.ui.base.BaseFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.math.roundToInt

class MapFragment : BaseFragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentMapBinding
    private val viewModel: MapScreenViewModel by inject()
    private var currentMarker: Marker? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        askLocationPermission(::locationPermissionsGranted)
    }

    private fun locationPermissionsGranted() {
        setupMap()
    }

    private fun setupMap() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        map.setOnMapClickListener {
            binding.progressContainer.isVisible = true
            if (NetworkUtil.isNetworkAvailable(requireContext())
                == NetworkUtil.ConnectionStatus.NotConnected) {
                binding.progressContainer.isVisible = false
                showNoInternetDialog()
                return@setOnMapClickListener
            }
            currentMarker?.remove()
            map.addMarker(
                MarkerOptions().position(it)
            ).also { marker ->
                currentMarker = marker
            }
            viewModel.getWeatherDetailsForLocation(it.latitude, it.longitude)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.weatherDetailsState.collectLatest {
                        with(binding) {
                            if (it.isNotEmpty())
                                progressContainer.isVisible = false
                            detailsCard.isVisible = it.isNotEmpty()
                            address.text = it.location.address
                            details.text = getString(
                                R.string.details_template,
                                it.temperature.roundToInt(),
                                it.summary
                            )
                            if (it.iconUrl.isNotEmpty())
                                Picasso.get().load(it.iconUrl).into(weatherIcon)
                        }
                    }
                }
            }
        }
    }

    private fun showNoInternetDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.no_internet_dialog_title)
            .setMessage(R.string.no_internet_dialog_msg)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }.create()
        dialog.show()
    }
}