package com.nextus.kotlinmvvmexample.shared.data.user

import com.nextus.kotlinmvvmexample.shared.di.IoDispatcher
import com.nextus.kotlinmvvmexample.shared.domain.user.GetUserUseCase
import com.nextus.kotlinmvvmexample.shared.network.RemoteClient
import com.nextus.kotlinmvvmexample.shared.network.api.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class UserModule {

    @Singleton
    @Provides
    fun provideUserApi(): UserApi = RemoteClient.createRetrofit().create(UserApi::class.java)

    @Singleton
    @Provides
    fun provideUserRepository(userApi: UserApi): UserDataSource = UserDataSourceImpl(userApi)

   /* @Singleton
    @Provides
    fun provideGetUserUseCase(
            userRepository: UserRepository,
            @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): GetUserUseCase = GetUserUseCase(userRepository, ioDispatcher)*/

}