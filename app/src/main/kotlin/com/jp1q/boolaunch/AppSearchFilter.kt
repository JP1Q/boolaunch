package com.jp1q.boolaunch

object AppSearchFilter {

    fun filter(apps: List<AppInfo>, query: String): List<AppInfo> {
        val normalizedQuery = query.trim()
        if (normalizedQuery.isEmpty()) {
            return apps
        }

        return apps.filter { app ->
            app.label.contains(normalizedQuery, ignoreCase = true)
        }
    }
}
