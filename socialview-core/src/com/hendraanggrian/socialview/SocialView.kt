package com.hendraanggrian.socialview

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.ColorStateList.valueOf
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat.getColorStateList
import android.support.v4.util.PatternsCompat.WEB_URL
import kotlin.text.RegexOption.IGNORE_CASE

/**
 * Base methods of all socialview's widgets.
 * The implementation, however, are kept in [SocialViewImpl].
 *
 * @see SocialViewImpl
 */
interface SocialView {

    /** Shall be shadowed when implemented in [android.view.View]. */
    fun getContext(): Context

    /** Shall be shadowed when implemented in [android.widget.TextView]. */
    fun getText(): CharSequence

    /** Determine whether this view should find and span hashtags. */
    var isHashtagEnabled: Boolean

    /** Determine whether this view should find and span mentions. */
    var isMentionEnabled: Boolean

    /** Determine whether this view should find and span hyperlinks. */
    var isHyperlinkEnabled: Boolean

    /** Color of hashtag spans. Default is color accent of current app theme. */
    var hashtagColor: ColorStateList

    /** Color of mention spans. Default is color accent of current app theme. */
    var mentionColor: ColorStateList

    /** Color of hyperlink spans. Default is color accent of current app theme. */
    var hyperlinkColor: ColorStateList

    /** Set hashtag color from color integer. */
    fun setHashtagColor(@ColorInt color: Int) {
        hashtagColor = valueOf(color)
    }

    /** Set mention color from color integer. */
    fun setMentionColor(@ColorInt color: Int) {
        mentionColor = valueOf(color)
    }

    /** Set hyperlink color from color integer. */
    fun setHyperlinkColor(@ColorInt color: Int) {
        hyperlinkColor = valueOf(color)
    }

    /** Set hashtag color from color resources. E.g.: `R.color.myColor`. */
    fun setHashtagColorRes(@ColorRes resId: Int) {
        hashtagColor = getColorStateList(getContext(), resId)!!
    }

    /** Set mention color from color resources. E.g.: `R.color.myColor`. */
    fun setMentionColorRes(@ColorRes resId: Int) {
        mentionColor = getColorStateList(getContext(), resId)!!
    }

    /** Set hyperlink color from color resources. E.g.: `R.color.myColor`. */
    fun setHyperlinkColorRes(@ColorRes resId: Int) {
        hyperlinkColor = getColorStateList(getContext(), resId)!!
    }

    /** Set hashtag color from color attribute. E.g.: `R.attr.colorPrimary`. */
    fun setHashtagColorAttr(@AttrRes attrId: Int) {
        hashtagColor = valueOf(getContext().getColorAttr(attrId))
    }

    /** Set mention color from color attribute. E.g.: `R.attr.colorPrimary`. */
    fun setMentionColorAttr(@AttrRes attrId: Int) {
        mentionColor = valueOf(getContext().getColorAttr(attrId))
    }

    /** Set hyperlink color from color attribute. E.g.: `R.attr.colorPrimary`. */
    fun setHyperlinkColorAttr(@AttrRes attrId: Int) {
        hyperlinkColor = valueOf(getContext().getColorAttr(attrId))
    }

    /** Register a callback to be invoked when hashtag is clicked. */
    fun setOnHashtagClickListener(listener: ((view: SocialView, String) -> Unit)?)

    /** Register a callback to be invoked when mention is clicked. */
    fun setOnMentionClickListener(listener: ((view: SocialView, String) -> Unit)?)

    /** Register a callback to be invoked when hyperlink is clicked. */
    fun setOnHyperlinkClickListener(listener: ((view: SocialView, String) -> Unit)?)

    /** Register a text watcher to be invoked when hashtag is modified. */
    fun setHashtagTextChangedListener(watcher: ((view: SocialView, String) -> Unit)?)

    /** Register a text watcher to be invoked when mention is modified. */
    fun setMentionTextChangedListener(watcher: ((view: SocialView, String) -> Unit)?)

    /** Internal function to span text based on current configuration. */
    fun colorize()

    /** Obtain all hashtags in current text. */
    val hashtags: List<String> get() = if (!isHashtagEnabled) emptyList() else HASHTAG_PATTERN.newList

    /** Obtain all mentions in current text. */
    val mentions: List<String> get() = if (!isMentionEnabled) emptyList() else MENTION_PATTERN.newList

    /** Obtain all hyperlinks in current text. */
    val hyperlinks: List<String> get() = if (!isHyperlinkEnabled) emptyList() else HYPERLINK_PATTERN.newList

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
        internal var HASHTAG_PATTERN: Regex = "#(\\w+)".toRegex()
        internal var MENTION_PATTERN: Regex = "@(\\w+)".toRegex()
        internal var HYPERLINK_PATTERN: Regex = WEB_URL.toRegex()

        /** Change current hashtag regex. */
        fun setHashtagRegex(regex: String) {
            HASHTAG_PATTERN = regex.toRegex(IGNORE_CASE)
        }

        /** Change current mention regex. */
        fun setMentionRegex(regex: String) {
            MENTION_PATTERN = regex.toRegex(IGNORE_CASE)
        }

        /** Change current hyperlink regex. */
        fun setHyperlinkRegex(regex: String) {
            HYPERLINK_PATTERN = regex.toRegex(IGNORE_CASE)
        }
    }
}