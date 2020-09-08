package com.nextus.kotlinmvvmexample.shared.domain.weather

import com.nextus.kotlinmvvm.model.Weather
import com.nextus.kotlinmvvmexample.shared.data.weather.WeatherRepository
import com.nextus.kotlinmvvmexample.shared.di.IoDispatcher
import com.nextus.kotlinmvvmexample.shared.domain.FlowUseCase
import com.nextus.kotlinmvvmexample.shared.domain.UseCase
import com.nextus.kotlinmvvmexample.shared.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

open class GetWeathersUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
): FlowUseCase<Unit, List<Weather>>(dispatcher) {

    override fun execute(parameters: Unit): Flow<Result<List<Weather>>> {
        return weatherRepository.getWeathers().map { result ->
            when(result) {
                is Result.Success -> {
                    Result.Success(result.data)
                }
                is Result.Loading -> result
                is Result.Error -> result
            }
        }
    }

}