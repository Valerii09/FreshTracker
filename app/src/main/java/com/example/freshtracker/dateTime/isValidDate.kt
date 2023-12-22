package com.example.freshtracker.dateTime

import java.util.Calendar

fun isValidDate(dateString: String): Boolean {
    return try {
        val day = dateString.take(2).toInt()
        val month = dateString.substring(2, 4).toInt()
        val year = dateString.takeLast(4).toInt()

        // Проверка корректности введенной даты
        month in 1..12 && day in 1..getMaxDaysInMonth(month, year)
    } catch (e: Exception) {
        false
    }
}
private fun getMaxDaysInMonth(month: Int, year: Int): Int {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.MONTH, month - 1)
    calendar.set(Calendar.YEAR, year)
    return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
}