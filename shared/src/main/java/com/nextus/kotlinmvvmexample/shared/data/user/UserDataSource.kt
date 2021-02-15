package com.nextus.kotlinmvvmexample.shared.data.user

import com.google.gson.JsonObject
import com.nextus.kotlinmvvm.model.AppUser
import com.nextus.kotlinmvvmexample.shared.data.auth.RefreshToken
import com.nextus.kotlinmvvmexample.shared.network.api.UserApi
import com.nextus.kotlinmvvmexample.shared.result.Result
import com.nextus.kotlinmvvmexample.shared.result.UnauthorizedException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

interface UserDataSource {
    fun getUser(uuid: String): Flow<Result<AppUser>>
    fun createUser(userInfo: JsonObject): Flow<Result<AppUser>>
    fun isDuplicateNickname(nickname: String): Flow<Result<JsonObject>>
}

class UserDataSourceImpl @Inject constructor(
    private val userApi: UserApi
): UserDataSource {

    override fun getUser(uuid: String): Flow<Result<AppUser>> {
        return flow {
            emit(Result.Loading)

            val response = userApi.getUser(uuid).execute()

            when {
                response.isSuccessful -> {
                    response.body()?.let {
                        emit(Result.Success(it))
                    }
                }
                response.code() == 401 -> throw UnauthorizedException("Unauthorized")
                else -> emit(Result.Error(Exception("Network Error")))
            }
        }
    }

    override fun createUser(userInfo: JsonObject): Flow<Result<AppUser>> {
        return flow {
            emit(Result.Loading)

            val response = userApi.createUser(userInfo).execute()

            when {
                response.isSuccessful -> {
                    response.body()?.let {
                        emit(Result.Success(it))
                    }
                }
                response.code() == 401 -> throw UnauthorizedException("Unauthorized")
                else -> emit(Result.Error(Exception("Network Error")))
            }
        }
    }

    override fun isDuplicateNickname(nickname: String): Flow<Result<JsonObject>> {
        return flow {
            emit(Result.Loading)

            val response = userApi.isDuplicateNickname(nickname).execute()

            when {
                response.isSuccessful -> {
                    response.body()?.let {
                        emit(Result.Success(it))
                    }
                }
                response.code() == 401 -> throw UnauthorizedException("Unauthorized")
                else -> emit(Result.Error(Exception("Network Error")))
            }
        }
    }

}