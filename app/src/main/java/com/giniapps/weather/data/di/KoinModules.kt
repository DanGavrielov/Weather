package com.giniapps.weather.data.di

import com.giniapps.weather.data.cache.Cache
import com.giniapps.weather.data.cache.WeatherCache
import com.giniapps.weather.data.cache.room.WeatherDatabase
import com.giniapps.weather.data.location.LocationManager
import com.giniapps.weather.data.remote.DataSource
import com.giniapps.weather.data.remote.RemoteDataSource
import com.giniapps.weather.data.repository.MainRepository
import com.giniapps.weather.data.repository.Repository
import com.giniapps.weather.ui.InternetConnectionViewModel
import com.giniapps.weather.ui.main_screen.MainScreenViewModel
import com.giniapps.weather.ui.map_screen.MapScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val remoteModule = module {
    factory {
        CoroutineScope(Dispatchers.Main + Job())
    }
    factory<DataSource> {
        RemoteDataSource(
            context = androidContext(),
            scope = get()
        )
    }
}

val cacheModule = module {
    factory {
        WeatherDatabase.getDatabase(androidContext())
            .weatherDao()
    }

    factory<Cache> {
        WeatherCache(weatherDao = get())
    }
}

val repositoryModule = module {
    factory<Repository> {
        MainRepository(
            dataSource = get(),
            cache = get()
        )
    }
}

val viewModelModule = module {
    factory {
        LocationManager(context = androidContext())
    }

    viewModel {
        MainScreenViewModel(
            repository = get(),
            locationManager = get()
        )
    }

    viewModel {
        MapScreenViewModel(
            repository = get()
        )
    }

    single {
        InternetConnectionViewModel()
    }
}