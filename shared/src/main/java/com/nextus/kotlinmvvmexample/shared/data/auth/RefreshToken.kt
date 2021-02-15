package com.nextus.kotlinmvvmexample.shared.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.nextus.kotlinmvvmexample.shared.network.RemoteClient
import com.nextus.kotlinmvvmexample.shared.util.suspendAndWait
import timber.log.Timber

object RefreshToken {

    suspend fun refresh() {
        FirebaseAuth.getInstance().currentUser?.let { currentUser ->
            // Get the ID token (force refresh)
            val tokenTask = currentUser.getIdToken(true)
            try {
                val tokenResult: GetTokenResult = tokenTask.suspendAndWait()
                tokenResult.token?.let {
                    // Call registration point to generate a result in Firestore
                    RemoteClient.token = it
                    Timber.d("User authenticated, hitting registration endpoint")
                }
            } catch (e: Exception) {
                Timber.e(e)
                return@let
            }
        }
    }

}