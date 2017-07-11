package com.hendraanggrian.socialview

import android.content.Context
import android.support.annotation.AttrRes
import android.support.annotation.ColorRes
import com.hendraanggrian.support.utils.content.color
import com.hendraanggrian.support.utils.content.colorAttr

/**
 * Base methods of all socialview's widgets.
 * The logic, however, are calculated in [SociableViewImpl] while the widgets are just
 * passing these methods to the attacher.

 * @author Hendra Anggian (hendraanggrian@gmail.com)
 * *
 * @see SociableViewImpl
 */
interface SociableView {

    val internalContext: Context

    val hashtags: Collection<String>
    val mentions: Collection<String>
    val hyperlinks: Collection<String>

    var isHashtagEnabled: Boolean
    var isMentionEnabled: Boolean
    var isHyperlinkEnabled: Boolean

    var hashtagColor: Int
    var mentionColor: Int
    var hyperlinkColor: Int

    fun setHashtagColorRes(@ColorRes id: Int) {
        hashtagColor = id.color(internalContext)
    }

    fun setMentionColorRes(@ColorRes id: Int) {
        mentionColor = id.color(internalContext)
    }

    fun setHyperlinkColorRes(@ColorRes id: Int) {
        hyperlinkColor = id.color(internalContext)
    }

    fun setHashtagColorAttr(@AttrRes id: Int) {
        hashtagColor = id.colorAttr(internalContext, true)
    }

    fun setMentionColorAttr(@AttrRes id: Int) {
        mentionColor = id.colorAttr(internalContext, true)
    }

    fun setHyperlinkColorAttr(@AttrRes id: Int) {
        hyperlinkColor = id.colorAttr(internalContext, true)
    }

    fun setOnHashtagClickListener(listener: ((SociableView, CharSequence) -> Unit)?)
    fun setOnMentionClickListener(listener: ((SociableView, CharSequence) -> Unit)?)

    fun setHashtagTextChangedListener(watcher: ((SociableView, CharSequence) -> Unit)?)
    fun setMentionTextChangedListener(watcher: ((SociableView, CharSequence) -> Unit)?)
}