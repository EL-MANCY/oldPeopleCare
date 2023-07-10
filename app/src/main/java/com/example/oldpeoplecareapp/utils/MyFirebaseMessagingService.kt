package com.example.oldpeoplecareapp.utils

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.oldpeoplecareapp.sendNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService:FirebaseMessagingService() {


    override fun onNewToken(token: String) {
       val preferences =  getApplicationContext().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        preferences.edit().putString("FCMTOKEN", token).apply()
        Log.i(TAG,token)


    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
    }

    private fun sendNotification(messageBody: String) {
        val notificationManager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java) as NotificationManager
        notificationManager.sendNotification(messageBody, applicationContext)
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }

}