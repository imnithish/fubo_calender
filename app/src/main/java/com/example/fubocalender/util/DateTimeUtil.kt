/*
 * Created by Nitheesh Ag on 16/09/22, 6:19 AM
 */

package com.example.fubocalender.util

import android.icu.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

fun LocalTime.formatTime(): String {
    return LocalTime.parse(
        this.toString(),
        DateTimeFormatter.ofPattern("HH:mm")
    )
        .format(DateTimeFormatter.ofPattern("h:mm a"))
}

fun LocalDate.formatDate(): String {
    return LocalDate
        .now()
        .withMonth(this.monthValue)
        .withYear(this.year)
        .withDayOfMonth(this.dayOfMonth)
        .format(DateTimeFormatter.ofPattern("d-MMM-yyyy"))
}

fun Int.getTimeFromSeconds(
    dayText: String = "d",
    minuteText: String = "m",
    hourText: String = "hr",
    showInDays: Boolean = false
): String? {
    val s = StringBuilder()
    return try {
        if (showInDays) {
            val numberOfDays = this / 86400
            val hour = (this % 86400) / 3600
            val minute = ((this % 86400) % 3600) / 60
            if (numberOfDays != 0)
                when {
                    hour == 0 -> s.append("$numberOfDays $dayText $minute $minuteText")
                    minute == 0 -> s.append("$numberOfDays $dayText $hour $hourText")
                    else -> s.append("$numberOfDays $dayText ${(this % 86400) / 3600} $hourText ${((this % 86400) % 3600) / 60} $minuteText")
                }
            else
                when {
                    hour == 0 -> s.append("$minute $minuteText")
                    minute == 0 -> s.append("$hour $hourText")
                    else -> s.append("${(this % 86400) / 3600} $hourText ${((this % 86400) % 3600) / 60} $minuteText")
                }

            return s.toString()
        } else {
            val hours: Int = this / 3600
            val minutes: Int = this % 3600 / 60
            when {
                hours == 0 -> s.append("$minutes $minuteText")
                minutes == 0 -> s.append("$hours $hourText")
                else -> s.append("$hours $hourText $minutes $minuteText")
            }
            return s.toString()
        }
    } catch (e: Exception) {
        null
    }
}

infix fun String.duration(end: String): String? {
    val format = SimpleDateFormat("h:mm a", Locale.getDefault())
    val date1: Date = format.parse(this)
    val date2: Date = format.parse(end)
    val difference = date2.time - date1.time
    return (difference / 1000).toInt().getTimeFromSeconds()
}

fun mergeDateAndTime(date: String, time: String): LocalDateTime {
    val datePart = LocalDate.parse(date, DateTimeFormatter.ofPattern("d-MMM-yyyy"))
    val timePart = LocalTime.parse(time, DateTimeFormatter.ofPattern("h:mm a"))
    return LocalDateTime.of(datePart, timePart)
}