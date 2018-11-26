package com.hendraanggrian.appcompat.widget

import android.content.res.ColorStateList
import android.content.res.ColorStateList.valueOf
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.util.PatternsCompat.WEB_URL
import kotlin.text.RegexOption.IGNORE_CASE

/**
 * Base methods of all socialview's widgets.
 * The implementation, however, are kept in [SocialViewImpl].
 *
 * @see SocialViewImpl
 */
interface SocialView<T : TextView> {

    var flags: Int

    /**
     * Internal function to initialize [TextView] with [SocialView].
     * To be called immediately upon view creation.
     *
     * @param attrs from view's constructor.
     */
    fun T.setup(attrs: AttributeSet?)

    /** Determine whether this view should find and span hashtags. */
    fun isHashtagEnabled(): Boolean = FLAG_HASHTAG.isPresent()

    /** Enable or disable hashtag spanning, if not already. */
    fun setHashtagEnabled(enabled: Boolean) = FLAG_HASHTAG.modify(enabled, isHashtagEnabled())

    /** Determine whether this view should find and span mentions. */
    fun isMentionEnabled(): Boolean = FLAG_MENTION.isPresent()

    /** Enable or disable mention spanning, if not already. */
    fun setMentionEnabled(enabled: Boolean) = FLAG_MENTION.modify(enabled, isMentionEnabled())

    /** Determine whether this view should find and span hyperlinks. */
    fun isHyperlinkEnabled(): Boolean = FLAG_HYPERLINK.isPresent()

    /** Enable or disable hyperlink spanning, if not already. */
    fun setHyperlinkEnabled(enabled: Boolean) = FLAG_HYPERLINK.modify(enabled, isHyperlinkEnabled())

    /** Color of hashtag spans. Default is color accent of current app theme. */
    var hashtagColors: ColorStateList

    /** Color of mention spans. Default is color accent of current app theme. */
    var mentionColorStateList: ColorStateList

    /** Color of hyperlink spans. Default is color accent of current app theme. */
    var hyperlinkColors: ColorStateList

    /** Get and set hashtag color from color integer. */
    var hashtagColor: Int
        get() = hashtagColors.defaultColor
        set(value) {
            hashtagColors = valueOf(value)
        }

    /** Get and set mention color from color integer. */
    var mentionColor: Int
        get() = mentionColorStateList.defaultColor
        set(value) {
            mentionColorStateList = valueOf(value)
        }

    /** Get and set hyperlink color from color integer. */
    var hyperlinkColor: Int
        get() = hyperlinkColors.defaultColor
        set(value) {
            hyperlinkColors = valueOf(value)
        }

    /** Register a callback to be invoked when hashtag is clicked. */
    fun setOnHashtagClickListener(listener: ((view: T, String) -> Unit)?)

    /** Register a callback to be invoked when mention is clicked. */
    fun setOnMentionClickListener(listener: ((view: T, String) -> Unit)?)

    /** Register a callback to be invoked when hyperlink is clicked. */
    fun setOnHyperlinkClickListener(listener: ((view: T, String) -> Unit)?)

    /** Register a text watcher to be invoked when hashtag is modified. */
    fun setHashtagTextChangedListener(watcher: ((view: T, String) -> Unit)?)

    /** Register a text watcher to be invoked when mention is modified. */
    fun setMentionTextChangedListener(watcher: ((view: T, String) -> Unit)?)

    /** Obtain all hashtags in current text. */
    val hashtags: List<String>

    /** Obtain all mentions in current text. */
    val mentions: List<String>

    /** Obtain all hyperlinks in current text. */
    val hyperlinks: List<String>

    /** Internal method to span the text based on current configuration. */
    fun colorize()

    @Suppress("NOTHING_TO_INLINE")
    private inline fun Int.isPresent() = flags or this == flags

    @Suppress("NOTHING_TO_INLINE")
    private fun Int.modify(enabled: Boolean, initialState: Boolean) {
        if (enabled != initialState) {
            flags = when {
                enabled -> flags or this
                else -> flags and inv()
            }
            colorize()
        }
    }

    companion object {
        internal const val FLAG_HASHTAG = 1
        internal const val FLAG_MENTION = 2
        internal const val FLAG_HYPERLINK = 4

        internal var REGEX_HASHTAG: Regex = "#(\\w+)".toRegex()
        internal var REGEX_MENTION: Regex = "@(\\w+)".toRegex()
        internal var REGEX_HYPERLINK: Regex = WEB_URL.toRegex()

        /** Change current hashtag regex. */
        fun setHashtagRegex(regex: String) {
            REGEX_HASHTAG = regex.toRegex(IGNORE_CASE)
        }

        /** Change current mention regex. */
        fun setMentionRegex(regex: String) {
            REGEX_MENTION = regex.toRegex(IGNORE_CASE)
        }

        /** Change current hyperlink regex. */
        fun setHyperlinkRegex(regex: String) {
            REGEX_HYPERLINK = regex.toRegex(IGNORE_CASE)
        }
    }
}