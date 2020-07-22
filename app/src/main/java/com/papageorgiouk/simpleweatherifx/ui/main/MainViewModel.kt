package com.papageorgiouk.simpleweatherifx.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.papageorgiouk.simpleweatherifx.interactors.FetchWeatherForCityUseCase
import com.papageorgiouk.simpleweatherifx.interactors.LoadWeatherHistoryUseCase
import com.papageorgiouk.simpleweatherifx.ui.view.UiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
class MainViewModel(
    loadHistoryUseCase: LoadWeatherHistoryUseCase,
    val fetchWeatherForCityUseCase: FetchWeatherForCityUseCase
) : ViewModel() {

    private val _requestStatus = MutableLiveData<UiState>(UiState.Loading)  //  backing property
    val requestStatus: LiveData<UiState> = _requestStatus

    //  return as flow so we can manipulate for the adapter
    val historyFlow = loadHistoryUseCase()
        .onStart { _requestStatus.postValue(UiState.Loading) }
        .onEach { _requestStatus.postValue(UiState.Success) }
        .catch {
            _requestStatus.postValue(UiState.Error(Exception(it)))
            it.printStackTrace()
        }.map { it.asReversed() }  //  latest first
        .map {
            it.subList(0, minOf(it.size, MAX_ITEMS))  //  max 5 items in the list
        }

    fun onCityEntered(city: String) {
        _requestStatus.postValue(UiState.Loading)

        fetchWeatherForCityUseCase(city)
            .onEach { result ->
                when {
                    result.isSuccess -> result.getOrNull()?.let {
                        _requestStatus.postValue(
                            UiState.Success
                        )
                    }
                        ?: _requestStatus.postValue(
                            UiState.Error(
                                Exception("Empty response")
                            )
                        )
                    result.isFailure -> result.exceptionOrNull()
                        ?.let {
                            _requestStatus.postValue(
                                UiState.Error(
                                    Exception(it)
                                )
                            )
                        }
                        ?: _requestStatus.postValue(
                            UiState.Error(
                                Exception("Generic error")
                            )
                        )
                }
            }.catch {
                _requestStatus.postValue(
                    UiState.Error(
                        Exception(it)
                    )
                )
                it.printStackTrace()
            }.launchIn(viewModelScope)
    }

}