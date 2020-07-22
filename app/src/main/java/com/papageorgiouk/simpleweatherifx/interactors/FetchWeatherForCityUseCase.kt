package com.papageorgiouk.simpleweatherifx.interactors

import com.papageorgiouk.simpleweatherifx.data.WeatherRepository

class FetchWeatherForCityUseCase(private val weatherRepo: WeatherRepository) {

    operator fun invoke(city: String) = weatherRepo.getWeatherForCity(city)
}