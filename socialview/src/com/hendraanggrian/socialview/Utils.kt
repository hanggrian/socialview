@file:Suppress("NOTHING_TO_INLINE")

package com.hendraanggrian.socialview

/** Mini version of [kota][https://github.com/hendraanggrian/kota] */

import android.text.Spannable
import android.text.Spanned
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE

/** Get all spans that are instance of [type]. */
internal inline fun <reified T> Spanned.getSpans(type: Class<T>): Array<out T> = getSpans(0, length, type)

/** Remove [spans] from this text. */
internal inline fun Spannable.removeSpans(vararg spans: Any) {
    for (span in spans) removeSpan(span)
}

@JvmOverloads
internal inline fun Spannable.span(
        regex: Regex,
        vararg spans: (String) -> Any,
        flags: Int = SPAN_EXCLUSIVE_EXCLUSIVE
): Spannable {
    val matcher = regex.toPattern().matcher(this)
    while (matcher.find()) {
        val start = matcher.start()
        val end = matcher.end()
        spanRange(start, end, *spans, flags = flags)
    }
    return this
}

@JvmOverloads
internal inline fun Spannable.spanRange(
        start: Int,
        end: Int,
        vararg spans: (String) -> Any,
        flags: Int = SPAN_EXCLUSIVE_EXCLUSIVE
): Spannable {
    for (span in spans) setSpan(span(substring(start, end)), start, end, flags)
    return this
}