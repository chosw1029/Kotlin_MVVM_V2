package com.nextus.kotlinmvvmexample.shared.data.user

import com.google.gson.JsonObject
import com.nextus.kotlinmvvm.model.AppUser
import com.nextus.kotlinmvvmexample.shared.data.auth.RefreshToken
import com.nextus.kotlinmvvmexample.shared.result.Result
import com.nextus.kotlinmvvmexample.shared.result.UnauthorizedException
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class UserRepository @Inject constructor (
    private val userDataSource: UserDataSource
): UserDataSource {

    override fun getUser(uuid: String): Flow<Result<AppUser>> {
        return flow {
            userDataSource.getUser(uuid)
                    .retry(retries = 1) {
                        if(it is UnauthorizedException) {
                            RefreshToken.refresh()
                            true
                        } else
                            false
                    }
                    .collect {
                        emit(it)
                    }
        }
    }

    override fun createUser(userInfo: JsonObject): Flow<Result<AppUser>> {
        return flow {
            userDataSource.createUser(userInfo)
                    .retry(retries = 1) {
                        if(it is UnauthorizedException) {
                            RefreshToken.refresh()
                            true
                        } else
                            false
                    }
                    .collect {
                        emit(it)
                    }
        }
    }

    override fun isDuplicateNickname(nickname: String): Flow<Result<JsonObject>> {
        return flow {
            userDataSource.isDuplicateNickname(nickname)
                    .retry(retries = 1) {
                        if(it is UnauthorizedException) {
                            RefreshToken.refresh()
                            true
                        } else
                            false
                    }
                    .collect {
                        emit(it)
                    }
        }
    }
}