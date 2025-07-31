package com.ityu.studymvvmandroid.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun dayAndDate(dateTime: Date?): String {
        // Ensure dateTime is not null
        requireNotNull(dateTime)
        val dayFormatter = SimpleDateFormat("EEEE", Locale.getDefault())
        val dateFormatter = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
        return "${dayFormatter.format(dateTime)}, ${dateFormatter.format(dateTime)}"
    }

    fun militaryTime(dateTime: Date): String {
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        return formatter.format(dateTime)
    }
    fun formatTimestamp(timestamp: Long?, pattern: String = "hh:mma dd MMM yy", locale: Locale = Locale.getDefault()): String {
        if (timestamp == null || timestamp == 0L) return "N/A"
        return try {
            val sdf = SimpleDateFormat(pattern, locale)
            sdf.format(Date(timestamp))
        } catch (e: Exception) {
            "Invalid Date"
        }
    }

}