package com.nextus.kotlinmvvmexample.shared.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber


class FcmMessagingService : FirebaseMessagingService()  {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("New firebase token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.d("Message data payload: ${remoteMessage.data}")
        worker(remoteMessage)
    }

    private fun worker(remoteMessage: RemoteMessage) {
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
}