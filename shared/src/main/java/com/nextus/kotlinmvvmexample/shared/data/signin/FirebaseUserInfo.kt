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

package com.nextus.kotlinmvvmexample.shared.data.signin

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserInfo
import com.nextus.kotlinmvvm.model.AppUser

class FirebaseUserInfo(
    private val firebaseUser: FirebaseUser?
) : AuthenticatedUserInfo {

    val appUser = MutableLiveData<AppUser>()

    override fun isSignedIn(): Boolean = firebaseUser != null

    override fun isRegistered(): Boolean = appUser.value != null

    override fun getFirebaseUser(): FirebaseUser? = firebaseUser

    override fun getAppUser(): AppUser? = appUser.value

    override fun setAppUser(user: AppUser?) {
        appUser.postValue(user)
    }

    override fun getEmail(): String? = firebaseUser?.email

    override fun getProviderData(): MutableList<out UserInfo>? = firebaseUser?.providerData

    override fun isAnonymous(): Boolean? = firebaseUser?.isAnonymous

    override fun getPhoneNumber(): String? = firebaseUser?.phoneNumber

    override fun getUid(): String? = firebaseUser?.uid

    override fun isEmailVerified(): Boolean? = firebaseUser?.isEmailVerified

    override fun getDisplayName(): String? = appUser.value?.nickname

    override fun getPhotoUrl(): Uri? = firebaseUser?.photoUrl

    override fun getProviderId(): String? = firebaseUser?.providerId

    override fun getLastSignInTimestamp(): Long? = firebaseUser?.metadata?.lastSignInTimestamp

    override fun getCreationTimestamp(): Long? = firebaseUser?.metadata?.creationTimestamp

}
