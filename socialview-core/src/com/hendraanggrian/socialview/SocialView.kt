package com.hendraanggrian.socialview

import android.content.Context
import android.content.res.ColorStateList
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.v4.util.PatternsCompat
import com.hendraanggrian.common.content.getColor2
import com.hendraanggrian.common.content.getColorStateList2
import com.hendraanggrian.common.content.toColorStateList
import java.util.regex.Pattern

/**
 * Base methods of all socialview's widgets.
 * The logic, however, are calculated in [SocialViewImpl] while the widgets are just
 * passing these methods to the [SocialViewHolder].
 *
 * @author Hendra Anggian (hendraanggrian@gmail.com)
 * @see SocialViewImpl
 */
interface SocialView {

    fun getContext(): Context
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

    fun setHashtagColor(@ColorInt color: Int): Unit {
        hashtagColor = getContext().getColor2(color).toColorStateList()
    }

    fun setMentionColor(@ColorInt color: Int): Unit {
        mentionColor = getContext().getColor2(color).toColorStateList()
    }

    fun setHyperlinkColor(@ColorInt color: Int): Unit {
        hyperlinkColor = getContext().getColor2(color).toColorStateList()
    }

    fun setHashtagColorRes(@ColorRes id: Int): Unit {
        hashtagColor = getContext().getColorStateList2(id)
    }

    fun setMentionColorRes(@ColorRes id: Int): Unit {
        mentionColor = getContext().getColorStateList2(id)
    }

    fun setHyperlinkColorRes(@ColorRes id: Int): Unit {
        hyperlinkColor = getContext().getColorStateList2(id)
    }

    fun setHashtagColorAttr(@AttrRes id: Int): Unit {
        hashtagColor = getContext().theme.getColorStateList2(id)
    }

    fun setMentionColorAttr(@AttrRes id: Int): Unit {
        mentionColor = getContext().theme.getColorStateList2(id)
    }

    fun setHyperlinkColorAttr(@AttrRes id: Int): Unit {
        hyperlinkColor = getContext().theme.getColorStateList2(id)
    }

    fun setOnHashtagClickListener(listener: ((SocialView, CharSequence) -> Unit)?): Unit
    fun setOnMentionClickListener(listener: ((SocialView, CharSequence) -> Unit)?): Unit
    fun setOnHyperlinkClickListener(listener: ((SocialView, CharSequence) -> Unit)?): Unit

    fun setHashtagTextChangedListener(watcher: ((SocialView, CharSequence) -> Unit)?): Unit
    fun setMentionTextChangedListener(watcher: ((SocialView, CharSequence) -> Unit)?): Unit

    fun colorize(): Unit

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