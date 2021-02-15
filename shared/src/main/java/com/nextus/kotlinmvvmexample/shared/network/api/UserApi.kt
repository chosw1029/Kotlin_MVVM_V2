package com.nextus.kotlinmvvmexample.shared.network.api

import com.google.gson.JsonObject
import com.nextus.kotlinmvvm.model.AppUser
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserApi {

    @FormUrlEncoded
    @POST("user/isDuplicateNickname")
    fun isDuplicateNickname(@Field("nickname") nickname: String): Call<JsonObject>

    @POST("user/createUser")
    fun createUser(@Body userInfo: JsonObject): Call<AppUser>

    @FormUrlEncoded
    @POST("user/getUser")
    fun getUser(@Field("uid") uuid: String): Call<AppUser>

}