package com.jp1q.boolaunch

import org.junit.Assert.assertEquals
import org.junit.Test

class AsciiRuleFormatterTest {

    @Test
    fun `buildRuleText repeats characters to fill drawable width`() {
        val renderedRule = AsciiRuleFormatter.buildRuleText(
            ruleCharacter = '=',
            drawableWidth = 100,
            singleCharacterWidth = 10f
        )
        assertEquals("==========", renderedRule)
    }

    @Test
    fun `buildRuleText falls back to single character when width is zero`() {
        val renderedRule = AsciiRuleFormatter.buildRuleText(
            ruleCharacter = '-',
            drawableWidth = 0,
            singleCharacterWidth = 8f
        )
        assertEquals("-", renderedRule)
    }
}
