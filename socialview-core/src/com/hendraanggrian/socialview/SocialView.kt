package com.hendraanggrian.socialview

import android.content.Context
import android.content.res.ColorStateList
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.v4.util.PatternsCompat
import com.hendraanggrian.common.content.getColorStateList2
import com.hendraanggrian.common.content.toColorStateList
import java.util.regex.Pattern

/**
 * Base methods of all socialview's widgets.
 * The logic, however, are calculated in [SocialViewImpl].
 *
 * @author Hendra Anggian (hendraanggrian@gmail.com)
 * @see SocialViewImpl
 */
interface SocialView {

    /**
     * If extended in [android.view.View], this function is already implemented.
     */
    fun getContext(): Context

    /**
     * If extended in [android.widget.TextView], this function is already implemented.
     */
    fun getText(): CharSequence

    var isHashtagEnabled: Boolean
    var isMentionEnabled: Boolean
    var isHyperlinkEnabled: Boolean

    var hashtagColor: ColorStateList
    var mentionColor: ColorStateList
    var hyperlinkColor: ColorStateList

    val hashtags: List<String> get() = HASHTAG_PATTERN.newList(isHashtagEnabled)
    val mentions: List<String> get() = MENTION_PATTERN.newList(isMentionEnabled)
    val hyperlinks: List<String> get() = HYPERLINK_PATTERN.newList(isHyperlinkEnabled)

    fun setHashtagColor(@ColorInt color: Int) {
        hashtagColor = color.toColorStateList()
    }

    fun setMentionColor(@ColorInt color: Int) {
        mentionColor = color.toColorStateList()
    }

    fun setHyperlinkColor(@ColorInt color: Int) {
        hyperlinkColor = color.toColorStateList()
    }

    fun setHashtagColorRes(@ColorRes id: Int) {
        hashtagColor = getContext().getColorStateList2(id)
    }

    fun setMentionColorRes(@ColorRes id: Int) {
        mentionColor = getContext().getColorStateList2(id)
    }

    fun setHyperlinkColorRes(@ColorRes id: Int) {
        hyperlinkColor = getContext().getColorStateList2(id)
    }

    fun setHashtagColorAttr(@AttrRes id: Int) {
        hashtagColor = getContext().theme.getColorStateList2(id)
    }

    fun setMentionColorAttr(@AttrRes id: Int) {
        mentionColor = getContext().theme.getColorStateList2(id)
    }

    fun setHyperlinkColorAttr(@AttrRes id: Int) {
        hyperlinkColor = getContext().theme.getColorStateList2(id)
    }

    fun setOnHashtagClickListener(listener: ((SocialView, CharSequence) -> Unit)?)
    fun setOnMentionClickListener(listener: ((SocialView, CharSequence) -> Unit)?)
    fun setOnHyperlinkClickListener(listener: ((SocialView, CharSequence) -> Unit)?)

    fun setHashtagTextChangedListener(watcher: ((SocialView, CharSequence) -> Unit)?)
    fun setMentionTextChangedListener(watcher: ((SocialView, CharSequence) -> Unit)?)

    fun colorize()

    private fun Pattern.newList(condition: Boolean): List<String> {
        if (!condition) {
            return emptyList()
        }
        val list = ArrayList<String>()
        val matcher = matcher(getText())
        while (matcher.find()) {
            list.add(matcher.group(if (this !== HYPERLINK_PATTERN) 1 /* remove hashtag and mention symbol */ else 0))
        }
        return list
    }

    companion object {
        internal const val FLAG_HASHTAG = 1
        internal const val FLAG_MENTION = 2
        internal const val FLAG_HYPERLINK = 4

        internal var HASHTAG_PATTERN = Pattern.compile("#(\\w+)")
        internal var MENTION_PATTERN = Pattern.compile("@(\\w+)")
        internal var HYPERLINK_PATTERN = PatternsCompat.WEB_URL

        /**
         * Change current hashtag pattern.
         */
        fun setHashtagPattern(regex: String) {
            HASHTAG_PATTERN = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
        }

        /**
         * Change current mention pattern.
         */
        fun setMentionPattern(regex: String) {
            MENTION_PATTERN = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
        }

        /**
         * Change current hyperlink pattern.
         */
        fun setHyperlinkPattern(regex: String) {
            HYPERLINK_PATTERN = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
        }
    }
}