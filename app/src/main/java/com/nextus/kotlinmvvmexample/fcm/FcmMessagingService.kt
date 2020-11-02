package com.nextus.kotlinmvvmexample.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.nextus.kotlinmvvmexample.R
import com.nextus.kotlinmvvmexample.shared.data.prefs.SharedPreferenceStorage
import com.nextus.kotlinmvvmexample.shared.fcm.FcmWorker
import com.nextus.kotlinmvvmexample.ui.ContainerActivity
import com.nextus.kotlinmvvmexample.ui.launcher.LauncherActivity
import com.nextus.kotlinmvvmexample.util.isAppInForeground
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
        Timber.e("From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Timber.d("Message data payload: ${remoteMessage.data}")

            if (remoteMessage.data["type"] == "Back") {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob(remoteMessage)
            } else {
                // Handle message within 10 seconds
                //handleNow()
                sendNotification(remoteMessage.data["title"]!!, remoteMessage.data["body"]!!)
            }
        }


        /*val myData: Data = workDataOf(
            "bid" to remoteMessage.data["item"],
            "title" to remoteMessage.data["title"],
            "body" to remoteMessage.data["body"],
            "imageUrl" to remoteMessage.data["imageUrl"],
            "type" to remoteMessage.data["type"]
        )

        val work = OneTimeWorkRequest.Builder(FcmWorker::class.java).setInputData(myData).build()
        WorkManager.getInstance(this).beginWith(work).enqueue()*/
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(title: String, body: String) {
        val notificationManager: NotificationManager = getSystemService()
                ?: throw Exception("Notification Manager not found.")

        if (!sharedPreferencesStorage.preferToReceiveCommentAndTagNotifications) {
            Timber.d("User disabled notifications, not showing")
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            makeNotificationChannelForPreSession(notificationManager)
        }

        val pendingIntent = NavDeepLinkBuilder(this)
                .setGraph(R.navigation.nav_graph)
                .setComponentName(if(isAppInForeground()) ContainerActivity::class.java else LauncherActivity::class.java)
                .setDestination(R.id.navigation_main)
                .createPendingIntent()

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_hero)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_hero))
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        notificationManager.notify(System.currentTimeMillis().toInt() /* ID of notification */, notificationBuilder.build())
    }

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
    }
}