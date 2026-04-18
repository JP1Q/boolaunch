package com.jp1q.boolaunch

enum class ColorScheme(val preferenceValue: String) {
    CRIMSON("crimson"),
    EMERALD("emerald");

    companion object {
        fun fromPreferenceValue(value: String?): ColorScheme {
            return entries.firstOrNull { it.preferenceValue == value } ?: CRIMSON
        }
    }
}
