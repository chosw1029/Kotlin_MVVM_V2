package com.nextus.kotlinmvvmexample.shared.fcm

import android.app.*
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.nextus.kotlinmvvmexample.shared.R
import com.nextus.kotlinmvvmexample.shared.data.prefs.SharedPreferenceStorage
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class FcmMessagingService : FirebaseMessagingService()  {

    @Inject
    lateinit var sharedPreferencesStorage: SharedPreferenceStorage

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("New firebase token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.d("Message data payload: ${remoteMessage.data}")
        worker(remoteMessage)
    }

    private fun worker(remoteMessage: RemoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Timber.d("From: ${remoteMessage.from}")
        val data = remoteMessage.data

        if(data[TRIGGER_COMMENT_AND_TAG_NOTIFICATION_KEY] == TRIGGER_COMMENT_AND_TAG_NOTIFICATION) {
            createCommentAndTagIntent(data["title"], data["body"], data["profileUrl"])
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun createCommentAndTagIntent(title: String?, body: String?, profileImageUrl: String?) {
        val notificationManager: NotificationManager = getSystemService()
                ?: throw Exception("Notification Manager not found.")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            makeNotificationChannelForPreSession(notificationManager)
        }

        if (!sharedPreferencesStorage.preferToReceiveCommentAndTagNotifications) {
            Timber.d("User disabled notifications, not showing")
            return
        }

        val intent = Intent(
                Intent.ACTION_VIEW,
                "".toUri()
                //"iosched://sessions?$QUERY_SESSION_ID=${session.id}".toUri()
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        // Create the TaskStackBuilder
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this)
                // Add the intent, which inflates the back stack
                .addNextIntentWithParentStack(intent)
                // Get the PendingIntent containing the entire back stack
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this, FcmWorker.CHANNEL_ID)
                .setContentTitle(title)
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                .setSmallIcon(R.drawable.ic_hero)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        if(profileImageUrl.isNullOrEmpty()) {
            notification.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_hero))
        } else {
            try {
                val largeIcon = Glide.with(this)
                        .asBitmap()
                        .load(getProfileImageUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .submit()

                notification.setLargeIcon(largeIcon.get())
                Glide.with(this).clear(largeIcon)
            } catch (e: Exception) {
                Timber.e(e)
                notification.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_hero))
            }
        }

        notificationManager.notify(System.currentTimeMillis().toInt(), notification.build())
    }

    private fun getProfileImageUrl(): String {
        return ""
    }

    /**
     * 오래걸리는 작업 처리
     */
    private fun scheduleJob(remoteMessage: RemoteMessage) {
        val myData: Data = workDataOf(
            "title" to remoteMessage.data["title"]
        )

        val work = OneTimeWorkRequest.Builder(FcmWorker::class.java).setInputData(myData).build()
        WorkManager.getInstance(this).beginWith(work).enqueue()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun makeNotificationChannelForPreSession(
            notificationManager: NotificationManager
    ) {
        notificationManager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                enableLights(true)
                enableVibration(true)
                setShowBadge(true)
            }
        )
    }

    companion object {
        const val CHANNEL_ID = "NextUs"
        const val CHANNEL_NAME = "NextUsChannel"
        const val TRIGGER_COMMENT_AND_TAG_NOTIFICATION = "COMMENT_AND_TAG_NOTIFICATION"
        const val TRIGGER_COMMENT_AND_TAG_NOTIFICATION_KEY = "action"
    }
}