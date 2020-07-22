package com.papageorgiouk.simpleweatherifx.interactors

import com.papageorgiouk.simpleweatherifx.data.WeatherRepository
import com.papageorgiouk.simpleweatherifx.domain.WeatherDataEntity
import kotlinx.coroutines.flow.Flow

class LoadWeatherHistoryUseCase(private val weatherRepo: WeatherRepository) {

    operator fun invoke(): Flow<List<WeatherDataEntity?>> = weatherRepo.getHistory()

}