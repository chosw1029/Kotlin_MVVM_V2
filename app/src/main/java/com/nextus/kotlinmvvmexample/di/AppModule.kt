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

import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageInfo
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nextus.kotlinmvvmexample.shared.analytics.AnalyticsHelper
import com.nextus.kotlinmvvmexample.shared.data.prefs.PreferenceStorage
import com.nextus.kotlinmvvmexample.shared.data.prefs.SharedPreferenceStorage
import com.nextus.kotlinmvvmexample.shared.di.ApplicationScope
import com.nextus.kotlinmvvmexample.shared.di.DefaultDispatcher
import com.nextus.kotlinmvvmexample.shared.network.RemoteClient
import com.nextus.kotlinmvvmexample.shared.network.api.WeatherApi
import com.nextus.kotlinmvvmexample.ui.signin.SignInViewModelDelegate
import com.nextus.kotlinmvvmexample.util.FirebaseAnalyticsHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

/**
 * Defines all the classes that need to be provided in the scope of the app.
 *
 * Define here all objects that are shared throughout the app, like SharedPreferences, navigators or
 * others. If some of those objects are singletons, they should be annotated with `@Singleton`.
 */
@InstallIn(ApplicationComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun providePreferenceStorage(@ApplicationContext context: Context): PreferenceStorage =
        SharedPreferenceStorage(context)

    @Provides
    fun providePackageInfo(@ApplicationContext context: Context): PackageInfo =
            context.packageManager.getPackageInfo(context.packageName, 0)

    @Provides
    fun provideWifiManager(@ApplicationContext context: Context): WifiManager =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
            as ConnectivityManager

    @Provides
    fun provideClipboardManager(@ApplicationContext context: Context): ClipboardManager =
        context.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE)
            as ClipboardManager

    @ApplicationScope
    @Singleton
    @Provides
    fun providesApplicationScope(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)

    @Singleton
    @Provides
    fun provideAnalyticsHelper(
        @ApplicationScope applicationScope: CoroutineScope,
        @ApplicationContext context: Context,
        signInDelegate: SignInViewModelDelegate
    ): AnalyticsHelper = FirebaseAnalyticsHelper(applicationScope, context, signInDelegate)

    @Singleton
    @Provides
    fun provideWeatherApi(): WeatherApi = RemoteClient.createRetrofit().create(WeatherApi::class.java)


   /* @Singleton
    @Provides
    @MainThreadHandler
    fun provideMainThreadHandler(): IOSchedHandler = IOSchedMainHandler()



    @Singleton
    @Provides
    fun provideAgendaRepository(appConfigDataSource: AppConfigDataSource): AgendaRepository =
        DefaultAgendaRepository(appConfigDataSource)

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.buildDatabase(context)
    }*/

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }
}
