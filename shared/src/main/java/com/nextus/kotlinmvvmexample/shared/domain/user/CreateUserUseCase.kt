package com.nextus.kotlinmvvmexample.shared.domain.user

import com.google.gson.JsonObject
import com.nextus.kotlinmvvm.model.AppUser
import com.nextus.kotlinmvvmexample.shared.data.user.UserRepository
import com.nextus.kotlinmvvmexample.shared.di.IoDispatcher
import com.nextus.kotlinmvvmexample.shared.domain.FlowUseCase
import com.nextus.kotlinmvvmexample.shared.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

open class CreateUserUseCase @Inject constructor(
        private val userRepository: UserRepository,
        @IoDispatcher dispatcher: CoroutineDispatcher
): FlowUseCase<JsonObject, AppUser>(dispatcher) {

    override fun execute(parameters: JsonObject): Flow<Result<AppUser>> {
        return userRepository.createUser(parameters).map { result ->
            when(result) {
                is Result.Success -> Result.Success(result.data)
                is Result.Loading -> result
                is Result.Error -> result
                is Result.Unauthorized -> result
            }
        }
    }
}