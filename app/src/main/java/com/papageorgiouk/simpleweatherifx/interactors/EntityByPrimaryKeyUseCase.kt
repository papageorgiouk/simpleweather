package com.papageorgiouk.simpleweatherifx.interactors

import com.papageorgiouk.simpleweatherifx.core.DatabaseDataSource
import com.papageorgiouk.simpleweatherifx.domain.WeatherDataEntity

class EntityByPrimaryKeyUseCase(private val db: DatabaseDataSource<WeatherDataEntity>) {

    suspend operator fun invoke(key: Int): WeatherDataEntity = db.getByPrimaryKey(key)

}