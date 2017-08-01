package com.hendraanggrian.socialview

import android.support.annotation.AttrRes
import android.support.annotation.ColorRes
import android.text.Spannable
import android.text.style.CharacterStyle
import android.text.style.ForegroundColorSpan
import android.util.Patterns
import android.view.View
import android.widget.TextView
import com.hendraanggrian.kota.content.getColor
import com.hendraanggrian.kota.content.getColor2
import com.hendraanggrian.kota.text.putSpansAll
import com.hendraanggrian.kota.text.removeAllSpans
import java.util.regex.Pattern

/**
 * Base methods of all socialview's widgets.
 * The logic, however, are calculated in [SocialViewHelper] while the widgets are just
 * passing these methods to the attacher.
 *
 * @author Hendra Anggian (hendraanggrian@gmail.com)
 * @see SocialViewHelper
 */
interface SocialView {

    val view: TextView

    var isHashtagEnabled: Boolean
    var isMentionEnabled: Boolean
    var isHyperlinkEnabled: Boolean

    var hashtagColor: Int
    var mentionColor: Int
    var hyperlinkColor: Int

    val hashtags: List<String> get() = newList(isHashtagEnabled, HASHTAG_PATTERN)
    val mentions: List<String> get() = newList(isMentionEnabled, MENTION_PATTERN)
    val hyperlinks: List<String> get() = newList(isHyperlinkEnabled, Patterns.WEB_URL)

    fun setHashtagColorRes(@ColorRes id: Int) {
        hashtagColor = view.context.getColor2(id)
    }

    fun setMentionColorRes(@ColorRes id: Int) {
        mentionColor = view.context.getColor2(id)
    }

    fun setHyperlinkColorRes(@ColorRes id: Int) {
        hyperlinkColor = view.context.getColor2(id)
    }

    fun setHashtagColorAttr(@AttrRes id: Int) {
        hashtagColor = view.context.theme.getColor(id, true)
    }

    fun setMentionColorAttr(@AttrRes id: Int) {
        mentionColor = view.context.theme.getColor(id, true)
    }

    fun setHyperlinkColorAttr(@AttrRes id: Int) {
        hyperlinkColor = view.context.theme.getColor(id, true)
    }

    fun getOnHashtagClickListener(): ((SocialView, CharSequence) -> Unit)?
    fun getOnMentionClickListener(): ((SocialView, CharSequence) -> Unit)?

    fun setOnHashtagClickListener(listener: ((SocialView, CharSequence) -> Unit)?)
    fun setOnMentionClickListener(listener: ((SocialView, CharSequence) -> Unit)?)

    fun setHashtagTextChangedListener(watcher: ((SocialView, CharSequence) -> Unit)?)
    fun setMentionTextChangedListener(watcher: ((SocialView, CharSequence) -> Unit)?)

    fun colorize() {
        val spannable = view.text
        check(spannable is Spannable, { "Attached text is not a Spannable, add TextView.BufferType.SPANNABLE when setting text to this TextView." })
        spannable as Spannable
        spannable.removeAllSpans(CharacterStyle::class.java)
        if (isHashtagEnabled) {
            getOnHashtagClickListener().let {
                spannable.putSpansAll(HASHTAG_PATTERN, {
                    if (it == null) {
                        ForegroundColorSpan(hashtagColor)
                    } else {
                        object : NoUnderlineClickableSpan(hashtagColor) {
                            override fun onClick(widget: View) = it.invoke(this@SocialView, spannable.subSequence(spannable.getSpanStart(this) + 1, spannable.getSpanEnd(this)))
                        }
                    }
                })
            }
        }
        if (isMentionEnabled) {
            getOnMentionClickListener().let {
                spannable.putSpansAll(MENTION_PATTERN, {
                    if (it == null) {
                        ForegroundColorSpan(mentionColor)
                    } else {
                        object : NoUnderlineClickableSpan(mentionColor) {
                            override fun onClick(widget: View) = it.invoke(this@SocialView, spannable.subSequence(spannable.getSpanStart(this) + 1, spannable.getSpanEnd(this)))
                        }
                    }
                })
            }
        }
        if (isHyperlinkEnabled) {
            spannable.putSpansAll(Patterns.WEB_URL, {
                SimpleURLSpan(spannable, hyperlinkColor)
            })
        }
    }

    fun detach()

    private fun newList(condition: Boolean, pattern: Pattern): List<String> {
        if (!condition) {
            return emptyList()
        }
        val list = ArrayList<String>()
        val matcher = pattern.matcher(view.text)
        while (matcher.find()) {
            list.add(matcher.group(if (pattern != Patterns.WEB_URL) 1 /* remove hashtag and mention symbol */ else 0))
        }
        return list
    }

    companion object {
        internal const val FLAG_HASHTAG = 1
        internal const val FLAG_MENTION = 2
        internal const val FLAG_HYPERLINK = 4

        private var HASHTAG_PATTERN = Pattern.compile("#(\\w+)")
        private var MENTION_PATTERN = Pattern.compile("@(\\w+)")

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
    }
}