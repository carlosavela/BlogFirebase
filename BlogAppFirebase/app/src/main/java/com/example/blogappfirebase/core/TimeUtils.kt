package com.example.blogappfirebase.core

import java.time.DayOfWeek
import java.util.concurrent.TimeUnit
private const val SECOND_MILLIS = 1
private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
private const val DAY_MILLIS = 24 * HOUR_MILLIS

object TimeUtils {
    fun getTimeAgo(time: Int) : String{

        // obtener la fehca en segundos
        val now = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
        if (time > now || time <= 0 ){
            return ""
        }

        val diff = now - time
        return when {
            diff < MINUTE_MILLIS -> "Just Now"
            diff < 2 * MINUTE_MILLIS -> "A minute ago"
            diff < 60 * MINUTE_MILLIS -> "${diff / MINUTE_MILLIS} nminutes ago"
            diff < 2 * HOUR_MILLIS -> "an hour ago"
            diff < 24 * HOUR_MILLIS -> "${diff / HOUR_MILLIS} hours ago"
            diff < 48 * HOUR_MILLIS -> "yesterday"
            else -> "${diff / DAY_MILLIS } days ago"
        }
    }
}