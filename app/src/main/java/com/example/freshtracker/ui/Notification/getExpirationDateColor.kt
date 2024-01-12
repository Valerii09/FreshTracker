package com.example.freshtracker.ui.Notification

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import java.util.Date

@Composable
fun getExpirationDateColor(expirationDate: Date): Color {
    return when {
        isExpirationDatePassed(expirationDate) -> Color(236, 45, 45, 255) // Срок годности истек
        isExpirationDateApproaching(expirationDate) -> Color(
            160,
            105,
            2,
            255
        ) // Оранжевый цвет, если остался один день
        else -> Color.Black // Остальные случаи
    }
}