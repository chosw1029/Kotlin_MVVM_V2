package com.nextus.kotlinmvvmexample.shared.data.weather

import com.nextus.kotlinmvvm.model.Weather
import com.nextus.kotlinmvvmexample.shared.network.api.WeatherApi
import javax.inject.Inject
import com.nextus.kotlinmvvmexample.shared.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface WeatherDataSource {
    fun getWeathers(): Flow<Result<List<Weather>>>
}

class PublicWeatherDataSource @Inject constructor(
    private val weatherApi: WeatherApi
): WeatherDataSource {

    override fun getWeathers(): Flow<Result<List<Weather>>> {
        val response = weatherApi.getWeathers().execute()

        return flow {
            if(response.isSuccessful) {
                response.body()?.let {
                    emit(Result.Success(it))
                }
            } else {
                emit(Result.Error(Exception("Network Error")))
            }
        }
    }

}