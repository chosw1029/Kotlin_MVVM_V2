package com.nextus.kotlinmvvmexample.shared.domain.weather

import com.nextus.kotlinmvvm.model.Weather
import com.nextus.kotlinmvvmexample.shared.data.weather.WeatherRepository
import com.nextus.kotlinmvvmexample.shared.di.IoDispatcher
import com.nextus.kotlinmvvmexample.shared.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

open class GetWeathersUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
): UseCase<Unit, List<Weather>>(dispatcher) {

    override suspend fun execute(parameters: Unit): List<Weather> {
        return weatherRepository.getWeathers()
    }

}