package com.jp1q.boolaunch

object TuiTextFormatter {

    private const val LEFT_BRACKET = "[ "
    private const val RIGHT_BRACKET = " ]"

    fun formatLauncherItemLabel(appLabel: String): String {
        return LEFT_BRACKET + appLabel + RIGHT_BRACKET
    }
}
