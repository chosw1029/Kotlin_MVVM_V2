package com.nextus.kotlinmvvmexample.shared.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.*
import android.os.Build
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nextus.kotlinmvvmexample.shared.R
import kotlinx.coroutines.coroutineScope
import java.util.concurrent.TimeUnit

class FcmWorker @WorkerInject constructor(
        @Assisted private val context: Context,
        @Assisted parameters: WorkerParameters
): CoroutineWorker(context, parameters) {

    override suspend fun doWork() = coroutineScope {
        /*val jobs = async {
            // example
            *//*
            getRelatedCardInfoUseCase(inputData.getString("bid")!!).collect { result ->
                        when(result) {
                            is com.nextus.kotlinmvvmexample.shared.result.Result.Success -> {
                                sendNotification(
                                        inputData.getString("title")!!,
                                        inputData.getString("body")!!,
                                        inputData.getString("imageUrl")!!,
                                        inputData.getString("type")!!,
                                        Gson().toJson(result.data.cards[0])
                                )
                            }
                            is com.nextus.kotlinmvvmexample.shared.result.Result.Error -> {
                                ListenableWorker.Result.failure()
                            }
                            else -> {}
                        }
                    }
             *//*

        }

        jobs.await()*/
        sendNotification(inputData.getString("title"))
        Result.success()
    }

    //, message: String, imageUrl: String, type: String, item: String
    private fun sendNotification(title: String?) {
        val notificationManager: NotificationManager = context.getSystemService()
                ?: throw Exception("Notification Manager not found.")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            makeNotificationChannel(context, notificationManager)
        }


        /*
                val largeIcon = Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .submit()

        notificationBuilder = NotificationCompat.Builder(context, channel)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE)

        try {
            val bitmap = largeIcon.get()
            notificationBuilder.setLargeIcon(if(imageUrl.isNullOrEmpty()) BitmapFactory.decodeResource(context.resources, R.drawable.ic_hero) else getCircularBitmap(bitmap))
        } catch (e: Exception) {
            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_hero))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notiChannel = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelMessage = NotificationChannel(channel, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
                description = "설명"
                enableLights(true)
                enableVibration(true)
                setShowBadge(true)
                vibrationPattern = longArrayOf(100L, 200L, 100L, 200L)
            }

            notiChannel.createNotificationChannel(channelMessage)
        }

        Glide.with(context).clear(largeIcon)

        var pendingIntent: PendingIntent? = null
*/
        /*
        "UserBoard" -> {
                val boardItem = Gson().fromJson(item, UserBoardModel::class.java)
                val bundle = Bundle().apply {
                    putParcelable("userBoardItem", boardItem.mapToPresentation())
                }

                pendingIntent = NavDeepLinkBuilder(context)
                    .setGraph(R.navigation.nav_graph)
                    .setComponentName(ContainerActivity::class.java)
                    .setDestination(R.id.navigation_user_board_detail)
                    .setArguments(bundle)
                    .createPendingIntent()
            }
         */

        /*notificationBuilder.setContentIntent(pendingIntent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())*/

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(context.getString(R.string.settings_send_anonymous_usage_statistics))
                .setSmallIcon(R.drawable.ic_hero)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_hero))
                .setTimeoutAfter(TimeUnit.MINUTES.toMillis(20)) // Backup (cancelled with receiver)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
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