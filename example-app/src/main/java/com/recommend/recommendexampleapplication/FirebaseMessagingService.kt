package com.recommend.recommendexampleapplication

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.recommend.sdk.Recommend

class FirebaseMessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Recommend.getMessagingService().processMessage(message.data)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Recommend.getMessagingService().setPushToken(token)
    }
}
