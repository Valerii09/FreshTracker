package com.example.freshtracker.dateTime

fun formatExpirationDate(input: String): String {
    val digitsOnly = input.filter { it.isDigit() }

    if (digitsOnly.length >= 2) {
        val month = digitsOnly.substring(0, 2)
        val year = digitsOnly.substring(2).take(4)

        return "$month.$year"
    }

    return digitsOnly
}