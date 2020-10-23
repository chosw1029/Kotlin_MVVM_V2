/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nextus.kotlinmvvmexample.shared.domain.auth

import com.nextus.kotlinmvvmexample.shared.data.signin.AuthenticatedUserInfo
import com.nextus.kotlinmvvmexample.shared.data.signin.AuthenticatedUserInfoBasic
import com.nextus.kotlinmvvmexample.shared.data.signin.FirebaseUserInfo
import com.nextus.kotlinmvvmexample.shared.data.signin.source.AuthStateUserDataSource
import com.nextus.kotlinmvvmexample.shared.di.ApplicationScope
import com.nextus.kotlinmvvmexample.shared.di.IoDispatcher
import com.nextus.kotlinmvvmexample.shared.domain.FlowUseCase
import com.nextus.kotlinmvvmexample.shared.domain.user.GetUserUseCase
import com.nextus.kotlinmvvmexample.shared.result.Result
import com.nextus.kotlinmvvmexample.shared.util.cancelIfActive
import com.nextus.kotlinmvvmexample.shared.fcm.TopicSubscriber
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A [FlowUseCase] that observes two data sources to generate an [AuthenticatedUserInfo]
 * that includes whether the user is registered (is an attendee).
 *
 * [AuthStateUserDataSource] provides general user information, like user IDs, while
 * whether the user is registered.
 */
@Singleton
open class ObserveUserAuthStateUseCase @Inject constructor(
        private val authStateUserDataSource: AuthStateUserDataSource,
        private val getUserUseCase: GetUserUseCase,
        private val topicSubscriber: TopicSubscriber,
        @ApplicationScope private val externalScope: CoroutineScope,
        @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : FlowUseCase<Any, AuthenticatedUserInfo>(ioDispatcher) {

    private var observeUserRegisteredChangesJob: Job? = null

    override fun execute(parameters: Any): Flow<Result<AuthenticatedUserInfo>> {
        // As a separate coroutine needs to listen for user registration changes, a channelFlow
        // is used instead of any other operator on authStateUserDataSource.getBasicUserInfo
        return channelFlow {
            authStateUserDataSource.getBasicUserInfo().collect { userResult ->
                Timber.e("getBaiscUserInfo")
                // Cancel observing previous user registered changes
                observeUserRegisteredChangesJob.cancelIfActive()

                if (userResult is Result.Success) {
                    if (userResult.data != null) {
                        processUserData(userResult.data)
                    } else {
                        channel.offer(Result.Success(FirebaseUserInfo(userResult.data))) // userResult.data == null
                    }
                } else {
                    channel.offer(Result.Error(Exception("FirebaseAuth error")))
                }
            }
        }
    }

   /* private fun subscribeToRegisteredTopic() {
        topicSubscriber.subscribeToAttendeeUpdates()
    }

    private fun unsubscribeFromRegisteredTopic() {
        topicSubscriber.unsubscribeFromAttendeeUpdates()
    }*/

    private suspend fun ProducerScope<Result<AuthenticatedUserInfo>>.processUserData(
        userData: AuthenticatedUserInfoBasic
    ) {
        Timber.e("ProcessUserData")
        if (!userData.isSignedIn()) {
            userSignedOut(userData)
        } else if (userData.getUid() != null) {
            userSignedIn(userData.getUid()!!, userData)
        } else {
            channel.offer(Result.Success(userData as FirebaseUserInfo))
        }
    }

    private suspend fun ProducerScope<Result<AuthenticatedUserInfo>>.userSignedIn(
            userId: String,
            userData: AuthenticatedUserInfoBasic
    ) {
        // Observing the user registration changes from another scope as doing it using a
        // supervisorScope was keeping the coroutine busy and updates to
        // authStateUserDataSource.getBasicUserInfo() were ignored
        observeUserRegisteredChangesJob = externalScope.launch(ioDispatcher) {
            // Start observing the user in Firestore to fetch the `registered` flag
            getUserUseCase(userId).collect { result ->
                when(result) {
                    is Result.Success -> {
                        channel.offer(
                            Result.Success(FirebaseUserInfo(userData.getFirebaseUser()).apply {
                                if(result.data.isNotEmpty())
                                    setAppUser(result.data[0])
                                else
                                    setAppUser(null)
                            })
                        )
                    }
                    else -> {
                        channel.offer(
                            Result.Success(FirebaseUserInfo(userData.getFirebaseUser()))
                        )
                    }
                }
            }
        }
    }

    private fun ProducerScope<Result<AuthenticatedUserInfo>>.userSignedOut(
        userData: AuthenticatedUserInfoBasic?
    ) {
        channel.offer(Result.Success(userData as FirebaseUserInfo))
        //unsubscribeFromRegisteredTopic() // Stop receiving notifications for attendees
    }
}
