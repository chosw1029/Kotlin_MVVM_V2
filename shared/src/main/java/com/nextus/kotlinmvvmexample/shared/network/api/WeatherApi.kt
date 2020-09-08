package com.nextus.kotlinmvvmexample.shared.network.api

import com.nextus.kotlinmvvm.model.Weather
import retrofit2.Call
import retrofit2.http.GET

interface WeatherApi {

    @GET("api/location/44418/2020/4/27")
    fun getWeathers(): Call<List<Weather>>

}