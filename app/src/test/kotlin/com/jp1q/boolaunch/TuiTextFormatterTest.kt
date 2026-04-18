package com.jp1q.boolaunch

import org.junit.Assert.assertEquals
import org.junit.Test

class TuiTextFormatterTest {

    @Test
    fun `formatLauncherItemLabel wraps app label with tui brackets`() {
        val formattedLabel = TuiTextFormatter.formatLauncherItemLabel("Settings")
        assertEquals("[ Settings ]", formattedLabel)
    }
}
