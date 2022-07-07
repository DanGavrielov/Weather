package com.giniapps.weather.ui.main_screen

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.giniapps.weather.R
import com.giniapps.weather.data.models.isEmpty
import com.giniapps.weather.databinding.FragmentMainScreenBinding
import com.giniapps.weather.ui.base.BaseFragment
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.math.roundToInt

class MainScreenFragment : BaseFragment() {
    private val viewModel: MainScreenViewModel by inject()
    private lateinit var binding: FragmentMainScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainScreenBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbarActionItems()
        askLocationPermission(::locationPermissionsGranted)
    }

    private fun setToolbarActionItems() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_screen_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.other_locations ->
                        findNavController()
                            .navigate(R.id.action_mainScreenFragment_to_mapFragment)
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun locationPermissionsGranted() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.weatherDetailsState.collectLatest {
                        with(binding) {
                            progressContainer.isVisible = it.isEmpty()
                            address.text = it.location.address
                            temp.text = getString(
                                R.string.temperature_template,
                                it.temperature.roundToInt()
                            )
                            summary.text = it.summary
                            if (it.iconUrl.isNotEmpty())
                                Picasso.get().load(it.iconUrl).into(weatherIcon)
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "MainScreenFragDebug"
    }
}