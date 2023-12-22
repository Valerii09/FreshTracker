package com.example.freshtracker.dateTime

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class ExpirationDateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.trim()
        val digitsOnly = trimmed.filter { it.isDigit() }

        if (digitsOnly.length > 10) {
            // Если длина строки превышает 10 символов, не обрабатывать ввод
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
            offset <= 5 -> 3 // в пределах месяца, перескакиваем в конец дня
            else -> 6 // в пределах года, перескакиваем в конец месяца
        }
    }

    override fun transformedToOriginal(offset: Int): Int {
        return offset
    }
}

