package com.nextus.kotlinmvvmexample.shared.di

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.nextus.kotlinmvvmexample.shared.BuildConfig
import com.nextus.kotlinmvvmexample.shared.R
import com.nextus.kotlinmvvmexample.shared.fcm.FcmTopicSubscriber
import com.nextus.kotlinmvvmexample.shared.fcm.TopicSubscriber
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class SharedModule {

    @Singleton
    @Provides
    fun provideTopicSubscriber(): TopicSubscriber {
        return FcmTopicSubscriber()
    }

    @Singleton
    @Provides
    fun provideFirebaseRemoteConfigSettings(): FirebaseRemoteConfigSettings {
        return if (BuildConfig.DEBUG) {
            remoteConfigSettings { minimumFetchIntervalInSeconds = 0 }
        } else {
            remoteConfigSettings { }
        }
    }

    @Singleton
    @Provides
    fun provideFirebaseRemoteConfig(
            configSettings: FirebaseRemoteConfigSettings
    ): FirebaseRemoteConfig {
        return Firebase.remoteConfig.apply {
            setConfigSettingsAsync(configSettings)
            setDefaultsAsync(R.xml.remote_config_defaults)
        }
    }
}