package com.example.freshtracker.ui.Notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.freshtracker.R
import com.example.freshtracker.R.drawable.ic_notification_icon

// Функция для отправки уведомления
fun sendNotification(context: Context, title: String, content: String) {
    println("Sending notification: Title=$title, Content=$content")
    val notificationManager = ContextCompat.getSystemService(context, NotificationManager::class.java)
    if (notificationManager != null) {
        val notificationChannelId = "product_notification_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                notificationChannelId,
                "Product Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notificationBuilder = NotificationCompat.Builder(context, notificationChannelId)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(ic_notification_icon) // Замените на свою иконку
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(1, notificationBuilder.build())
    }
}