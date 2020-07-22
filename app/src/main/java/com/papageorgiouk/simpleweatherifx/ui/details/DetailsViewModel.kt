package com.papageorgiouk.simpleweatherifx.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.papageorgiouk.simpleweatherifx.domain.WeatherDataEntity
import com.papageorgiouk.simpleweatherifx.interactors.EntityByPrimaryKeyUseCase

class DetailsViewModel(private val entityByPrimaryKey: EntityByPrimaryKeyUseCase) : ViewModel() {

    fun entityByKey(key: Int): LiveData<WeatherDataEntity> = liveData { emit(entityByPrimaryKey(key)) }

}