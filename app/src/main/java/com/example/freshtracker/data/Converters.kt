package com.example.freshtracker.data

import androidx.room.TypeConverter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Converters {
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun fromDateToString(date: Date?): String? {
        return date?.let {
            val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            format.format(it)
        }
    }

    @TypeConverter
    fun fromStringToDate(value: String?): Date? {
        return value?.let {
            val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            try {
                format.parse(it)
            } catch (e: ParseException) {
                e.printStackTrace()
                null
            }
        }
    }
}
