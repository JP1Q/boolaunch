package com.jp1q.boolaunch

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date
import java.util.Locale

class HeaderStatusFormatterTest {

    @Test
    fun `formatBatteryStatus returns fallback when battery values are invalid`() {
        assertEquals("[ --% ]", HeaderStatusFormatter.formatBatteryStatus(-1, 100))
        assertEquals("[ --% ]", HeaderStatusFormatter.formatBatteryStatus(50, 0))
    }

    @Test
    fun `formatBatteryStatus returns rounded down percentage label`() {
        val statusLabel = HeaderStatusFormatter.formatBatteryStatus(55, 100)
        assertEquals("[ 55% ]", statusLabel)
    }

    @Test
    fun `formatCurrentTime wraps formatted time in tui brackets`() {
        val knownDate = Date(0L)
        val timeLabel = HeaderStatusFormatter.formatCurrentTime(knownDate, Locale.US)
        assertEquals("[ 00:00 ]", timeLabel)
    }

    @Test
    fun `formatAsciiClock renders multi-line ascii digits for current time`() {
        val knownDate = Date(0L)
        val asciiClock = HeaderStatusFormatter.formatAsciiClock(knownDate)
        assertEquals(
            " ###   ###       ###   ### \n" +
                "#   # #   #  #  #   # #   #\n" +
                "#   # #   #     #   # #   #\n" +
                "#   # #   #  #  #   # #   #\n" +
                " ###   ###       ###   ### ",
            asciiClock
        )
    }
}
