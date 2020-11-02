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

package com.nextus.kotlinmvvmexample.ui.signin

import androidx.lifecycle.*
import com.nextus.kotlinmvvm.model.AppUser
import com.nextus.kotlinmvvmexample.shared.data.signin.AuthenticatedUserInfo
import com.nextus.kotlinmvvmexample.shared.data.signin.FirebaseUserInfo
import com.nextus.kotlinmvvmexample.shared.di.IoDispatcher
import com.nextus.kotlinmvvmexample.shared.di.MainDispatcher
import com.nextus.kotlinmvvmexample.shared.domain.auth.ObserveUserAuthStateUseCase
import com.nextus.kotlinmvvmexample.shared.result.Event
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import com.nextus.kotlinmvvmexample.shared.result.Result

enum class SignInEvent {
    RequestSignIn, RequestSignOut
}

/**
 * Interface to implement sign-in functionality in a ViewModel.
 *
 * You can inject a implementation of this via Dagger2, then use the implementation as an interface
 * delegate to add sign in functionality without writing any code
 *
 * Example usage
 *
 * ```
 * class MyViewModel @Inject constructor(
 *     signInViewModelComponent: SignInViewModelDelegate
 * ) : ViewModel(), SignInViewModelDelegate by signInViewModelComponent {
 * ```
 */
interface SignInViewModelDelegate {
    /**
     * Live updated value of the current firebase user
     */
    val currentUserInfo: LiveData<AuthenticatedUserInfo?>
    val currentAppUserInfo: LiveData<AppUser?>

    /**
     * Live updated value of the current firebase users image url
     */
    val currentUserImageUri: LiveData<String?>

    /**
     * Emits Events when a sign-in event should be attempted
     */
    val performSignInEvent: MutableLiveData<Event<SignInEvent>>

    /**
     * Emit an Event on performSignInEvent to request sign-in
     */
    suspend fun emitSignInRequest()

    /**
     * Emit an Event on performSignInEvent to request sign-out
     */
    suspend fun emitSignOutRequest()

    fun updateAppUser(appUser: AppUser)

    fun observeSignedInUser(): LiveData<Boolean>

    fun isSignedIn(): Boolean

    fun isRegistered(): Boolean

    /**
     * Returns the current user ID or null if not available.
     */
    fun getUserId(): String?

    fun getNickname(): String?
}

/**
 * Implementation of SignInViewModelDelegate that uses Firebase's auth mechanisms.
 */
internal class FirebaseSignInViewModelDelegate @Inject constructor(
        observeUserAuthStateUseCase: ObserveUserAuthStateUseCase,
        @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
        @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : SignInViewModelDelegate {

    override val performSignInEvent = MutableLiveData<Event<SignInEvent>>()

    private val currentFirebaseUser: Flow<Result<AuthenticatedUserInfo?>> =
        observeUserAuthStateUseCase(Any()).map {
            Timber.e("observeUserAuthStateUseCase")
            if (it is Result.Error) {
                Timber.e(it.exception)
            }
            it
        }

    override val currentUserInfo: LiveData<AuthenticatedUserInfo?> = currentFirebaseUser.map {
        (it as? Result.Success)?.data
    }.asLiveData()

    override val currentAppUserInfo: LiveData<AppUser?> = currentUserInfo.switchMap {
        (it as FirebaseUserInfo).appUser
    }

    override val currentUserImageUri: LiveData<String?> = currentAppUserInfo.map {
        it?.imageUrl
    }

    private val isSignedIn: LiveData<Boolean> = currentUserInfo.map {
        Timber.e("currentUserInfoChanged")
        it?.isSignedIn() ?: false
    }

    private val isRegistered: LiveData<Boolean> = currentUserInfo.map {
        it?.isRegistered() ?: false
    }

    override suspend fun emitSignInRequest() = withContext(ioDispatcher) {
        // Refresh the notificationsPrefIsShown because it's used to indicate if the
        withContext(mainDispatcher) {
            performSignInEvent.value = Event(SignInEvent.RequestSignIn)
        }
    }

    override suspend fun emitSignOutRequest() = withContext(mainDispatcher) {
        performSignInEvent.value = Event(SignInEvent.RequestSignOut)
    }

    override fun updateAppUser(appUser: AppUser) {
        (currentUserInfo.value as FirebaseUserInfo).getAppUser()
    }

    override fun isSignedIn(): Boolean = isSignedIn.value == true

    override fun isRegistered(): Boolean = isRegistered.value == true

    override fun observeSignedInUser(): LiveData<Boolean> = isSignedIn

    override fun getUserId(): String? {
        return currentUserInfo.value?.getUid()
    }

    override fun getNickname(): String? {
        return currentAppUserInfo.value?.nickname
    }
}
