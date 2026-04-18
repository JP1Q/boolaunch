package com.jp1q.boolaunch

import org.junit.Assert.assertEquals
import org.junit.Test

class AppSearchFilterTest {

    private val apps = listOf(
        AppInfo(label = "Camera", packageName = "com.android.camera"),
        AppInfo(label = "Messages", packageName = "com.android.messages"),
        AppInfo(label = "Settings", packageName = "com.android.settings")
    )

    @Test
    fun `filter returns all apps when query is blank`() {
        val filtered = AppSearchFilter.filter(apps, "   ")
        assertEquals(apps, filtered)
    }

    @Test
    fun `filter matches app labels case-insensitively`() {
        val filtered = AppSearchFilter.filter(apps, "MES")
        assertEquals(listOf(apps[1]), filtered)
    }

    @Test
    fun `filter returns empty list when no labels match`() {
        val filtered = AppSearchFilter.filter(apps, "browser")
        assertEquals(emptyList<AppInfo>(), filtered)
    }
}
