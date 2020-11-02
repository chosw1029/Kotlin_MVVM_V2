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

package com.nextus.kotlinmvvmexample.shared.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.nextus.kotlinmvvmexample.shared.R
import com.nextus.kotlinmvvmexample.shared.data.prefs.SharedPreferenceStorage
import com.nextus.kotlinmvvmexample.shared.data.signin.source.AuthIdDataSource
import com.nextus.kotlinmvvmexample.shared.di.ApplicationScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Receives broadcast intents with information for session notifications.
 */
@AndroidEntryPoint
class AlarmBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var sharedPreferencesStorage: SharedPreferenceStorage

    @Inject
    lateinit var authIdDataSource: AuthIdDataSource

    @ApplicationScope
    @Inject
    lateinit var externalScope: CoroutineScope

    override fun onReceive(context: Context, intent: Intent) {
        Timber.d("Alarm received")

        val sessionId = intent.getStringExtra(EXTRA_SESSION_ID) ?: return
        val userId = authIdDataSource.getUserId() ?: run {
            Timber.e("No user ID, not showing notification.")
            return
        }

        val channel = intent.getStringExtra(EXTRA_NOTIFICATION_CHANNEL)

        when (channel) {
            CHANNEL_ID_UPCOMING -> {
                externalScope.launch {
                    checkThenShowNotification(context, sessionId, userId)
                }
            }
            else -> {
                // This shouldn't happen, but we don't want the app to crash. Only logging
                Timber.w("Broadcast with unknown channel received: $channel")
            }
        }
    }

    private fun checkThenShowNotification(
        context: Context,
        sessionId: String,
        userId: String
    ) {
        if (!sharedPreferencesStorage.preferToReceiveCommentAndTagNotifications) {
            Timber.d("User disabled notifications, not showing")
            return
        }

        Timber.d("Showing pre session notification for $sessionId, user $userId")

        notifyWithoutUserEvent(sessionId, context)
    }

    private fun notifyWithoutUserEvent(title: String, context: Context) {
        val notificationId = showNotification(context, title)
        //alarmManager.dismissNotificationInFiveMinutes(notificationId)
    }

    @WorkerThread
    private fun showNotification(context: Context, title: String): Int {
        val notificationManager: NotificationManager = context.getSystemService()
            ?: throw Exception("Notification Manager not found.")

        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            makeNotificationChannelForPreSession(context, notificationManager)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_UPCOMING)
            .setContentTitle(title)
            .setContentText(context.getString(R.string.settings_enable_notifications))
            .setSmallIcon(R.drawable.ic_hero)
            .setTimeoutAfter(TimeUnit.MINUTES.toMillis(10)) // Backup (cancelled with receiver)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()

        val notificationId = System.currentTimeMillis().toInt()
        notificationManager.notify(notificationId, notification)
        return notificationId
    }

    @RequiresApi(VERSION_CODES.O)
    private fun makeNotificationChannelForPreSession(
        context: Context,
        notificationManager: NotificationManager
    ) {
        notificationManager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID_UPCOMING,
                context.getString(R.string.settings_send_anonymous_usage_statistics),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { lockscreenVisibility = Notification.VISIBILITY_PUBLIC }
        )
    }

    companion object {
        const val EXTRA_NOTIFICATION_CHANNEL = "notification_channel"
        const val EXTRA_SESSION_ID = "user_event_extra"

        /** If this flag it set to true in session detail, the show rate dialog should be opened */
        const val EXTRA_SHOW_RATE_SESSION_FLAG = "show_rate_session_extra"

        const val QUERY_SESSION_ID = "session_id"
        const val CHANNEL_ID_UPCOMING = "upcoming_channel_id"
        const val CHANNEL_ID_FEEDBACK = "feedback_channel_id"
    }
}
