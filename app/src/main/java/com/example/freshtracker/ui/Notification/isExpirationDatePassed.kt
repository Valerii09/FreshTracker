package com.example.freshtracker.ui.Notification

import java.util.Date

// Функция для проверки истечения срока годности
fun isExpirationDatePassed(expirationDate: Date): Boolean {
    val currentTime = Date()
    return currentTime.time > expirationDate.time
}
