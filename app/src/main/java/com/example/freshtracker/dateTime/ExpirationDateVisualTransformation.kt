package com.example.freshtracker.dateTime

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class ExpirationDateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.trim()
        val digitsOnly = trimmed.filter { it.isDigit() }

        if (digitsOnly.length > 8) {
            // Если длина строки превышает 8 символов, не обрабатывать ввод
            return TransformedText(AnnotatedString(digitsOnly), OffsetMapping.Identity)
        }

        val transformedText = when {
            digitsOnly.length <= 2 -> {
                TransformedText(AnnotatedString(digitsOnly), OffsetMapping.Identity)
            }
            digitsOnly.length <= 4 -> {
                val day = digitsOnly.substring(0, 2)
                val month = digitsOnly.substring(2)
                TransformedText(AnnotatedString("$day.$month"), DateOffsetMapping())
            }
            digitsOnly.length <= 6 -> {
                val day = digitsOnly.substring(0, 2)
                val month = digitsOnly.substring(2, 4)
                val year = digitsOnly.substring(4)
                TransformedText(AnnotatedString("$day.$month.$year"), DateOffsetMapping())
            }
            else -> {
                val day = digitsOnly.substring(0, 2)
                val month = digitsOnly.substring(2, 4)
                val year = digitsOnly.substring(4, minOf(8, digitsOnly.length))
                TransformedText(AnnotatedString("$day.$month.$year"), DateOffsetMapping())
            }
        }

        return transformedText
    }
}

class DateOffsetMapping : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int {
        return when {
            offset <= 2 -> offset // в пределах дня
            offset <= 5 -> offset + 1 // в пределах месяца, увеличиваем на 1
            else -> offset + 2 // в пределах года, увеличиваем на 2
        }
    }

    override fun transformedToOriginal(offset: Int): Int {
        return when {
            offset <= 2 -> offset // в пределах дня
            offset <= 5 -> 5 // в пределах месяца, возвращаем конец дня
            else -> 8 // в пределах года, возвращаем конец месяца
        }
    }
}


