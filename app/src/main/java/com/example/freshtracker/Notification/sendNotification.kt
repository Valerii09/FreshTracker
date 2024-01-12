package com.example.freshtracker.Notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.freshtracker.R
import com.example.freshtracker.R.drawable.ic_notification_icon

// Функция для отправки уведомления
fun sendNotification(context: Context, title: String, content: String) {
    val notificationIntent = Intent(context, NotificationIntentService::class.java)
    notificationIntent.putExtra("title", title)
    notificationIntent.putExtra("content", content)

    // Запускаем службу уведомлений
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Log.d("NotificationService", "Starting foreground service")
        context.startForegroundService(notificationIntent)
    } else {
        Log.d("NotificationService", "Starting regular service")
        context.startService(notificationIntent)
    }
}

