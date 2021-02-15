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

package com.nextus.kotlinmvvmexample.di

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.nextus.kotlinmvvmexample.R
import com.nextus.kotlinmvvmexample.shared.data.signin.source.AuthIdDataSource
import com.nextus.kotlinmvvmexample.shared.data.signin.source.AuthStateUserDataSource
import com.nextus.kotlinmvvmexample.shared.data.signin.source.FirebaseAuthStateUserDataSource
import com.nextus.kotlinmvvmexample.shared.di.ApplicationScope
import com.nextus.kotlinmvvmexample.shared.di.IoDispatcher
import com.nextus.kotlinmvvmexample.shared.di.MainDispatcher
import com.nextus.kotlinmvvmexample.shared.domain.notification.NotificationAlarmUpdater
import com.nextus.kotlinmvvmexample.shared.domain.user.GetUserUseCase
import com.nextus.kotlinmvvmexample.shared.fcm.FcmTokenUpdater
import com.nextus.kotlinmvvmexample.util.signin.FirebaseAuthSignInHandler
import com.nextus.kotlinmvvmexample.util.signin.SignInHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
internal class SignInModule {

    @Provides
    fun provideSignInHandler(
        @ApplicationScope applicationScope: CoroutineScope
    ): SignInHandler = FirebaseAuthSignInHandler(applicationScope)

    @Singleton
    @Provides
    fun provideAuthStateUserDataSource(
            firebase: FirebaseAuth,
            firestore: FirebaseFirestore,
            getUserUseCase: GetUserUseCase,
            notificationAlarmUpdater: NotificationAlarmUpdater,
            @ApplicationScope applicationScope: CoroutineScope,
            @IoDispatcher ioDispatcher: CoroutineDispatcher,
            @MainDispatcher mainDispatcher: CoroutineDispatcher
    ): AuthStateUserDataSource {
        return FirebaseAuthStateUserDataSource(
            firebase,
            FcmTokenUpdater(applicationScope, mainDispatcher, firestore),
            notificationAlarmUpdater,
            getUserUseCase,
            ioDispatcher
        )
    }

    @Singleton
    @Provides
    fun provideFirebaseFireStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Singleton
    @Provides
    fun provideFirebaseGso(@ApplicationContext context: Context): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
    }

    @Singleton
    @Provides
    fun provideGoogleSignInClient(@ApplicationContext context: Context, googleSignInOptions: GoogleSignInOptions): GoogleSignInClient {
        return GoogleSignIn.getClient(context, googleSignInOptions)
    }

    @Singleton
    @Provides
    fun providesAuthIdDataSource(
        firebaseAuth: FirebaseAuth
    ): AuthIdDataSource {
        return object : AuthIdDataSource {
            override fun getUserId() = firebaseAuth.currentUser?.uid
        }
    }
}
