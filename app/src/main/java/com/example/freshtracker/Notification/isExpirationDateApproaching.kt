package com.example.freshtracker.Notification

import java.util.Date

// Функция для проверки приближения срока годности
fun isExpirationDateApproaching(expirationDate: Date): Boolean {
    val currentTime = Date()
    val oneDayInMillis = 24 * 60 * 60 * 1000
    return expirationDate.time - currentTime.time <= oneDayInMillis
}