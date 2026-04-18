package com.jp1q.boolaunch

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object HeaderStatusFormatter {

    private const val UNKNOWN_BATTERY_STATUS = "[ --% ]"
    private const val TIME_PATTERN = "HH:mm"

    fun formatBatteryStatus(batteryLevel: Int, batteryScale: Int): String {
        if (batteryLevel < 0 || batteryScale <= 0) {
            return UNKNOWN_BATTERY_STATUS
        }
        val batteryPercent = (batteryLevel * 100) / batteryScale
        return "[ $batteryPercent% ]"
    }

    fun formatCurrentTime(currentDate: Date, locale: Locale): String {
        val timeFormatter = SimpleDateFormat(TIME_PATTERN, locale)
        return "[ ${timeFormatter.format(currentDate)} ]"
    }
}
