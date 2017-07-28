package com.hendraanggrian.socialview

import android.annotation.SuppressLint
import android.os.Parcel
import android.support.annotation.ColorInt
import android.text.Spannable
import android.text.TextPaint
import android.text.style.URLSpan

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
@SuppressLint("ParcelCreator")
internal class SimpleURLSpan(private val spannable: Spannable, @ColorInt private val color: Int) : URLSpan("") {

    override fun writeToParcel(dest: Parcel, flags: Int) = dest.writeString(url)

    override fun getURL() = spannable.subSequence(spannable.getSpanStart(this), spannable.getSpanEnd(this)).toString()

    override fun updateDrawState(ds: TextPaint) {
        ds.linkColor = color
        super.updateDrawState(ds)
    }
}