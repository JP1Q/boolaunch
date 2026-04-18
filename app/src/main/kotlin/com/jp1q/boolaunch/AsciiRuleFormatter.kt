package com.jp1q.boolaunch

import kotlin.math.floor
import kotlin.math.max

object AsciiRuleFormatter {

    fun buildRuleText(ruleCharacter: Char, drawableWidth: Int, singleCharacterWidth: Float): String {
        if (drawableWidth <= 0) {
            return ruleCharacter.toString()
        }

        val safeCharacterWidth = max(1f, singleCharacterWidth)
        val characterCount = max(1, floor(drawableWidth / safeCharacterWidth).toInt())
        return ruleCharacter.toString().repeat(characterCount)
    }
}
