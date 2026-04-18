package com.jp1q.boolaunch

import org.junit.Assert.assertEquals
import org.junit.Test

class AppInfoTest {

    @Test
    fun `AppInfo stores label and packageName correctly`() {
        val app = AppInfo(label = "Settings", packageName = "com.android.settings")
        assertEquals("Settings", app.label)
        assertEquals("com.android.settings", app.packageName)
    }

    @Test
    fun `AppInfo data class equality works`() {
        val app1 = AppInfo(label = "Camera", packageName = "com.android.camera")
        val app2 = AppInfo(label = "Camera", packageName = "com.android.camera")
        assertEquals(app1, app2)
    }

    @Test
    fun `AppInfo copy produces independent instance`() {
        val original = AppInfo(label = "Gallery", packageName = "com.android.gallery")
        val copy = original.copy(label = "Photos")
        assertEquals("Photos", copy.label)
        assertEquals("com.android.gallery", copy.packageName)
    }
}
