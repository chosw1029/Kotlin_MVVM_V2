package com.nextus.kotlinmvvmexample.shared.network.api

import com.google.gson.JsonObject
import com.nextus.kotlinmvvm.model.AppUser
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserApi {

    @FormUrlEncoded
    @POST("user/isDuplicateNickname")
    fun isDuplicateNickname(@Field("nickname") nickname: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("user/createUser")
    fun createUser(
        @Field("uuid") uuid: String,
        @Field("nickname") nickname: String,
        @Field("address") address: String?,
        @Field("email") email: String?,
        @Field("profileUrl") profile_url: String?,
    ): Call<AppUser>

    @FormUrlEncoded
    @POST("/user/findUserById")
    fun getUser(@Field("uid") uuid: String): Call<AppUser>

    @FormUrlEncoded
    @POST("/user/verifyToken")
    fun getFirebaseToken(
            @Field("token") token: String,
            @Field("isKakao") isKakao: String
    ): Call<JsonObject>
}