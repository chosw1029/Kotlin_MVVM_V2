package com.nextus.kotlinmvvmexample

import android.app.Application
import android.os.StrictMode
import com.nextus.kotlinmvvmexample.shared.analytics.AnalyticsHelper
import com.nextus.kotlinmvvmexample.util.CrashlyticsTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application() {

    // Even if the var isn't used, needs to be initialized at application startup.
    @Inject lateinit var analyticsHelper: AnalyticsHelper

    override fun onCreate() {
        // Enable strict mode before Dagger creates graph
        if (BuildConfig.DEBUG) {
            enableStrictMode()
        }
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashlyticsTree())
        }
    }

    private fun enableStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build()
        )
    }
}