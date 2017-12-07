@file:Suppress("NOTHING_TO_INLINE")

package com.hendraanggrian.widget

import android.support.annotation.IdRes
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE

/** Mini version of [kota][https://github.com/hendraanggrian/kota] */

inline fun <reified T : View> View.find(@IdRes id: Int): T = findViewById<T>(id) as T

internal inline var View.isVisible: Boolean
    get() = visibility == VISIBLE
    set(visible) {
        visibility = if (visible) VISIBLE else GONE
    }

internal inline fun <V : View> V.setVisibleThen(
        visible: Boolean,
        block: V.() -> Unit
) {
    isVisible = visible
    if (visibility == VISIBLE) block(this)
}