package com.example.freshtracker.ui.Notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.freshtracker.R

class MyNotificationService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannelId = "product_notification_channel"
            val notificationChannel = NotificationChannel(
                notificationChannelId,
                "Product Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)

            val notification = NotificationCompat.Builder(this, notificationChannelId)
                .setContentTitle("Foreground Service Title")
                .setContentText("Foreground Service Content")
                .setSmallIcon(R.drawable.ic_notification_icon)
                .build()

            startForeground(1, notification)
        }

        // Ваш код обработки уведомлений

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}