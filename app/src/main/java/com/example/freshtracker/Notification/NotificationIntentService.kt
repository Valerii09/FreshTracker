package com.example.freshtracker.Notification

import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.freshtracker.R

class NotificationIntentService : IntentService("NotificationIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val title = intent.getStringExtra("title")
            val content = intent.getStringExtra("content")

            // Запустить службу в foreground
            startForeground(1, Notification())

            Log.d("NotificationService", "Received intent with title: $title, content: $content")

            // Отправить уведомление
            sendNotification(title, content)
        }
    }


    private fun sendNotification(title: String?, content: String?) {
        try {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannelId = "product_notification_channel"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(
                    notificationChannelId,
                    "Product Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(notificationChannel)
            }

            val notificationBuilder = NotificationCompat.Builder(this, notificationChannelId)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_notification_icon) // Замените на свою иконку
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            notificationManager.notify(1, notificationBuilder.build())

            Log.d("NotificationService", "Notification sent successfully")
        } catch (e: Exception) {
            Log.e("NotificationService", "Error sending notification", e)
        }
    }
}