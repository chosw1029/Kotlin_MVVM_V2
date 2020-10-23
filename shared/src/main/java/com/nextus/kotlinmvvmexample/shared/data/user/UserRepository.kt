package com.nextus.kotlinmvvmexample.shared.data.user

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class UserRepository @Inject constructor (
    private val userDataSource: UserDataSource
): UserDataSource by userDataSource {
}