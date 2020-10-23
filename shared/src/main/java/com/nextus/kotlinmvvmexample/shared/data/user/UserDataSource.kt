package com.nextus.kotlinmvvmexample.shared.data.user

import com.nextus.kotlinmvvm.model.AppUser
import com.nextus.kotlinmvvmexample.shared.network.api.UserApi
import com.nextus.kotlinmvvmexample.shared.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface UserDataSource {
    /*fun isDuplicateNickname(nickname: String): Flow<Result<JsonObject>>

    fun createUser(
        uuid: String,
        nickname: String,
        address: String? = "",
        email: String? = "",
        profile_url: String? = ""
    ): Flow<Result<OnhealUser>>

    fun getUser(uuid: String): Flow<Result<List<OnhealUser>>>

    fun getFirebaseToken(token: String, isKakao: String): Flow<Result<JsonObject>>*/
    fun getUser(uuid: String): Flow<Result<List<AppUser>>>
}

class UserDataSourceImpl @Inject constructor(
    private val userApi: UserApi
): UserDataSource {

    override fun getUser(uuid: String): Flow<Result<List<AppUser>>> {
        return flow {
            val response = userApi.getUser(uuid).execute()

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