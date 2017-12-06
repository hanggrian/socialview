package com.hendraanggrian.socialview

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.ColorStateList.valueOf
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat.getColorStateList
import android.support.v4.util.PatternsCompat.WEB_URL
import kotlin.text.RegexOption.IGNORE_CASE

/**
 * Base methods of all socialview's widgets.
 * The logic, however, are calculated in [SocialViewImpl].
 *
 * @see SocialViewImpl
 */
interface SocialView {

    /** Shall be shadowed when implemented in [android.view.View]. */
    fun getContext(): Context

    /** Shall be shadowed when implemented in [android.widget.TextView]. */
    fun getText(): CharSequence

    var isHashtagEnabled: Boolean
    var isMentionEnabled: Boolean
    var isHyperlinkEnabled: Boolean

    var hashtagColor: ColorStateList
    var mentionColor: ColorStateList
    var hyperlinkColor: ColorStateList

    val hashtags: List<String> get() = if (!isHashtagEnabled) emptyList() else HASHTAG_PATTERN.newList
    val mentions: List<String> get() = if (!isMentionEnabled) emptyList() else MENTION_PATTERN.newList
    val hyperlinks: List<String> get() = if (!isHyperlinkEnabled) emptyList() else HYPERLINK_PATTERN.newList

    fun setHashtagColor(@ColorInt color: Int) {
        hashtagColor = valueOf(color)
    }

    fun setMentionColor(@ColorInt color: Int) {
        mentionColor = valueOf(color)
    }

    fun setHyperlinkColor(@ColorInt color: Int) {
        hyperlinkColor = valueOf(color)
    }

    fun setHashtagColorRes(@ColorRes id: Int) {
        hashtagColor = getColorStateList(getContext(), id)!!
    }

    fun setMentionColorRes(@ColorRes id: Int) {
        mentionColor = getColorStateList(getContext(), id)!!
    }

    fun setHyperlinkColorRes(@ColorRes id: Int) {
        hyperlinkColor = getColorStateList(getContext(), id)!!
    }

    fun setOnHashtagClickListener(listener: ((SocialView, CharSequence) -> Unit)?)
    fun setOnMentionClickListener(listener: ((SocialView, CharSequence) -> Unit)?)
    fun setOnHyperlinkClickListener(listener: ((SocialView, CharSequence) -> Unit)?)

    fun setHashtagTextChangedListener(watcher: ((SocialView, CharSequence) -> Unit)?)
    fun setMentionTextChangedListener(watcher: ((SocialView, CharSequence) -> Unit)?)

    fun colorize()

    private val Regex.newList: List<String>
        get() {
            val list = ArrayList<String>()
            val matcher = toPattern().matcher(getText())
            while (matcher.find()) {
                list.add(matcher.group(if (this !== HYPERLINK_PATTERN) 1 /* remove hashtag and mention symbol */ else 0))
            }
            return list
        }

    companion object {
        internal const val FLAG_HASHTAG = 1
        internal const val FLAG_MENTION = 2
        internal const val FLAG_HYPERLINK = 4

        internal var HASHTAG_PATTERN: Regex = "#(\\w+)".toRegex()
        internal var MENTION_PATTERN: Regex = "@(\\w+)".toRegex()
        internal var HYPERLINK_PATTERN: Regex = WEB_URL.toRegex()

        /** Change current hashtag pattern. */
        fun setHashtagPattern(regex: String) {
            HASHTAG_PATTERN = regex.toRegex(IGNORE_CASE)
        }

        /** Change current mention pattern. */
        fun setMentionPattern(regex: String) {
            MENTION_PATTERN = regex.toRegex(IGNORE_CASE)
        }

        /** Change current hyperlink pattern. */
        fun setHyperlinkPattern(regex: String) {
            HYPERLINK_PATTERN = regex.toRegex(IGNORE_CASE)
        }
    }
}