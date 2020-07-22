package com.papageorgiouk.simpleweatherifx

import androidx.room.Room
import com.papageorgiouk.simpleweatherifx.core.DatabaseDataSource
import com.papageorgiouk.simpleweatherifx.core.NetworkDataSource
import com.papageorgiouk.simpleweatherifx.core.WeatherBit
import com.papageorgiouk.simpleweatherifx.data.*
import com.papageorgiouk.simpleweatherifx.domain.WeatherDataEntity
import com.papageorgiouk.simpleweatherifx.interactors.EntityByPrimaryKeyUseCase
import com.papageorgiouk.simpleweatherifx.interactors.FetchWeatherForCityUseCase
import com.papageorgiouk.simpleweatherifx.interactors.LoadWeatherHistoryUseCase
import com.papageorgiouk.simpleweatherifx.ui.details.DetailsViewModel
import com.papageorgiouk.simpleweatherifx.ui.main.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val appmodule = module {

    single<DatabaseDataSource<WeatherDataEntity>> { Room.databaseBuilder(androidApplication(), WeatherDatabase::class.java, "weatherDb").build() }

    single<NetworkDataSource> { WeatherBit(androidApplication().getString(R.string.weatherbit_api_key)) }

    single<WeatherRepository> { WeatherRepository(get(), get()) }

    single { EntityByPrimaryKeyUseCase(get()) }
    single { FetchWeatherForCityUseCase(get()) }
    single { LoadWeatherHistoryUseCase(get()) }

    viewModel { MainViewModel(get(), get()) }
    viewModel { DetailsViewModel(get()) }

}