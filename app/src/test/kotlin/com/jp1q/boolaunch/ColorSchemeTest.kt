package com.jp1q.boolaunch

import org.junit.Assert.assertEquals
import org.junit.Test

class ColorSchemeTest {

    @Test
    fun `fromPreferenceValue returns matching color scheme`() {
        assertEquals(ColorScheme.EMERALD, ColorScheme.fromPreferenceValue("emerald"))
    }

    @Test
    fun `fromPreferenceValue falls back to crimson for unknown values`() {
        assertEquals(ColorScheme.CRIMSON, ColorScheme.fromPreferenceValue("unknown"))
    }

    @Test
    fun `fromPreferenceValue falls back to crimson for null values`() {
        assertEquals(ColorScheme.CRIMSON, ColorScheme.fromPreferenceValue(null))
    }
}
