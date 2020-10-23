package com.nextus.kotlinmvvmexample.shared.domain.user

import com.nextus.kotlinmvvm.model.AppUser
import com.nextus.kotlinmvvmexample.shared.data.user.UserRepository
import com.nextus.kotlinmvvmexample.shared.di.IoDispatcher
import com.nextus.kotlinmvvmexample.shared.domain.FlowUseCase
import com.nextus.kotlinmvvmexample.shared.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

open class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
): FlowUseCase<String, List<AppUser>>(dispatcher) {

    override fun execute(parameters: String): Flow<Result<List<AppUser>>> {
        return userRepository.getUser(parameters).map { result ->
            when(result) {
                is Result.Success -> Result.Success(result.data)
                is Result.Loading -> result
                is Result.Error -> result
            }
        }
    }
}