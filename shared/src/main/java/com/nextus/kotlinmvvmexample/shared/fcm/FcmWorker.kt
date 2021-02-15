package com.nextus.kotlinmvvmexample.shared.fcm

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Build
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nextus.kotlinmvvmexample.shared.R
import kotlinx.coroutines.coroutineScope
import timber.log.Timber
import java.util.concurrent.TimeUnit

class FcmWorker @WorkerInject constructor(
        @Assisted private val context: Context,
        @Assisted parameters: WorkerParameters
): CoroutineWorker(context, parameters) {

    override suspend fun doWork() = coroutineScope {
        sendNotification(inputData.getString("title"), inputData.getString("body"), inputData.getString("id"))
        Result.success()
    }

    private fun sendNotification(title: String?, body: String?, id: String?) {
        val notificationManager: NotificationManager = context.getSystemService()
                ?: throw Exception("Notification Manager not found.")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            makeNotificationChannel(context, notificationManager)
        }

        val intent = Intent(
                Intent.ACTION_VIEW,
                "".toUri()
                //"iosched://sessions?$QUERY_SESSION_ID=${session.id}".toUri()
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            //putExtra(EXTRA_SHOW_RATE_SESSION_FLAG, true)
        }

        // Create the TaskStackBuilder
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context)
                // Add the intent, which inflates the back stack
                .addNextIntentWithParentStack(intent)
                // Get the PendingIntent containing the entire back stack
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                .setSmallIcon(R.drawable.ic_hero)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        if(inputData.getString("imageUrl").isNullOrEmpty()) {
            notification.setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_hero))
        } else {
            try {
                val largeIcon = Glide.with(context)
                        .asBitmap()
                        .load(getProfileImageUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .submit()

                notification.setLargeIcon(largeIcon.get())
                Glide.with(context).clear(largeIcon)
            } catch (e: Exception) {
                Timber.e(e)
                notification.setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_hero))
            }
        }

        notificationManager.notify(System.currentTimeMillis().toInt(), notification.build())
    }

    private fun getProfileImageUrl(): String {
        return ""
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun makeNotificationChannel(
        context: Context,
        notificationManager: NotificationManager
    ) {
        notificationManager.createNotificationChannel(
            NotificationChannel(
                    CHANNEL_ID,
                    context.getString(R.string.default_notification_channel_id),
                    NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                enableLights(true)
                enableVibration(true)
                setShowBadge(true)
                vibrationPattern = longArrayOf(100L, 200L, 100L, 200L)
            }
        )
    }

    private fun getCircularBitmap(@NonNull bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(
            0, 0, bitmap.width,
            bitmap.height
        )
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawCircle(
            (bitmap.width / 2).toFloat(),
            (bitmap.height / 2).toFloat(), (bitmap.width / 2).toFloat(), paint
        )
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }

    companion object {
        const val CHANNEL_ID = "channel_id"
    }
}