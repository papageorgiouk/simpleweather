package com.papageorgiouk.simpleweatherifx.data

import com.papageorgiouk.simpleweatherifx.core.DatabaseDataSource
import com.papageorgiouk.simpleweatherifx.core.NetworkDataSource
import com.papageorgiouk.simpleweatherifx.domain.WeatherDataEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.IOException

class WeatherRepository(
    private val networkDataSource: NetworkDataSource,
    private val databaseDataSource: DatabaseDataSource<WeatherDataEntity>
) {

    fun getWeatherForCity(city: String): Flow<Result<WeatherDataEntity>> {
        return flow {
            //  network
            networkDataSource.getForCity(city)
                .onSuccess { list ->
                    list?.map { WeatherDataEntity(it) }
                        .let {
                            val datapoint = it!![0]  //  just get the first one, we don't need multiple ones
                            emit(Result.success(datapoint))

                            //  ...and update the db
                            saveToHistory(datapoint)
                        } ?: emit(Result.failure(IOException("Empty response")))
                }.onFailure {
                    it.printStackTrace()
                    emit(Result.failure(it))
                }
        }
    }

    fun getHistory(): Flow<List<WeatherDataEntity?>> = databaseDataSource.getHistory()

    /* Global scope because we don't want the db transaction to get cancelled if the UI scope clears */
    private fun saveToHistory(datapoint: WeatherDataEntity) {
        GlobalScope.launch { databaseDataSource.add(datapoint) }
    }

}