package com.okre.oreummark.common

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    /* 토큰 생성 메서드 */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    /* 메세지 수신 메서드 */
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
    }
}