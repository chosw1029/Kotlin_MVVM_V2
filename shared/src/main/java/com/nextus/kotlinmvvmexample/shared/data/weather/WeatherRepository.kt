package com.nextus.kotlinmvvmexample.shared.data.weather

import com.nextus.kotlinmvvm.model.Weather
import com.nextus.kotlinmvvmexample.shared.result.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class WeatherRepository @Inject constructor (
    private val weatherDataSource: WeatherDataSource
) {
    fun getWeathers(): Flow<Result<List<Weather>>> = weatherDataSource.getWeathers()
}