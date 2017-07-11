package com.hendraanggrian.socialview

import android.support.annotation.ColorInt
import android.text.TextPaint
import android.text.style.ClickableSpan

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
internal abstract class ForegroundColorClickableSpan(@param:ColorInt private val color: Int) : ClickableSpan() {

    override fun updateDrawState(ds: TextPaint) {
        ds.color = color
    }
}