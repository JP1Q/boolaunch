package com.jp1q.boolaunch

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class AsciiRuleTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var ruleCharacter: Char = '='

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AsciiRuleTextView)
        val configuredRuleCharacter = typedArray.getString(R.styleable.AsciiRuleTextView_ruleCharacter)
        typedArray.recycle()

        if (!configuredRuleCharacter.isNullOrEmpty()) {
            ruleCharacter = configuredRuleCharacter.first()
        }
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        updateRuleText()
    }

    private fun updateRuleText() {
        val drawableWidth = width - paddingLeft - paddingRight
        if (drawableWidth <= 0) {
            return
        }

        val renderedRule = AsciiRuleFormatter.buildRuleText(
            ruleCharacter = ruleCharacter,
            drawableWidth = drawableWidth,
            singleCharacterWidth = paint.measureText(ruleCharacter.toString())
        )
        if (text.toString() != renderedRule) {
            text = renderedRule
        }
    }
}
