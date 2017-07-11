package com.hendraanggrian.socialview

import android.content.Context
import android.text.Editable
import android.text.Spannable
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.CharacterStyle
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import android.widget.TextView
import com.hendraanggrian.support.utils.text.putSpansAll
import com.hendraanggrian.support.utils.text.removeSpans
import java.util.*
import java.util.regex.Pattern

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class SociableViewImpl<out T>(
        val view: T, // original View
        attrs: AttributeSet? = null // styles and shit
) : SociableView where T : TextView, T : SociableView {

    companion object {
        private const val TYPE_HASHTAG = 1
        private const val TYPE_MENTION = 2
        private const val TYPE_HYPERLINK = 4
        internal var PATTERN_HASHTAG = Pattern.compile("(?i)#([0-9A-Z_À-ÖØ-öø-ÿ]*[A-Z_]+[a-z0-9_üÀ-ÖØ-öø-ÿ]*)")
        internal var PATTERN_MENTION = Pattern.compile("(?i)@([0-9A-Z_À-ÖØ-öø-ÿ]*[A-Z_]+[a-z0-9_üÀ-ÖØ-öø-ÿ]*)")

        private fun newList(pattern: Pattern, input: CharSequence): Collection<String> {
            val matcher = pattern.matcher(input)
            val list = ArrayList<String>()
            while (matcher.find()) {
                list.add(matcher.group(if (pattern != Patterns.WEB_URL) 1 /* remove hashtag and mention symbol */ else 0))
            }
            return list
        }

        fun setHashtagPattern(regex: String) {
            PATTERN_HASHTAG = Pattern.compile(regex)
        }

        fun setMentionPattern(regex: String) {
            PATTERN_MENTION = Pattern.compile(regex)
        }
    }

    private val mTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            if (count > 0 && start > 0) {
                when (s[start - 1]) {
                    '#' -> {
                        isHashtagEditing = true
                        isMentionEditing = false
                    }
                    '@' -> {
                        isHashtagEditing = false
                        isMentionEditing = true
                    }
                    else -> if (!Character.isLetterOrDigit(s[start - 1])) {
                        isHashtagEditing = false
                        isMentionEditing = false
                    } else if (hashtagWatcher != null && isHashtagEditing) {
                        hashtagWatcher!!.invoke(view, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start - 1) + 1, start).toString())
                    } else if (mentionWatcher != null && isMentionEditing) {
                        mentionWatcher!!.invoke(view, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start - 1) + 1, start).toString())
                    }
                }
            }
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (s.isNotEmpty()) {
                colorize()
                // triggered when text is added
                if (start < s.length) {
                    if (start + count - 1 < 0) {
                        return
                    }
                    when (s[start + count - 1]) {
                        '#' -> {
                            isHashtagEditing = true
                            isMentionEditing = false
                        }
                        '@' -> {
                            isHashtagEditing = false
                            isMentionEditing = true
                        }
                        else -> if (!Character.isLetterOrDigit(s.get(start))) {
                            isHashtagEditing = false
                            isMentionEditing = false
                        } else if (hashtagWatcher != null && isHashtagEditing) {
                            hashtagWatcher!!.invoke(this@SociableViewImpl, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start) + 1, start + count).toString())
                        } else if (mentionWatcher != null && isMentionEditing) {
                            mentionWatcher!!.invoke(this@SociableViewImpl, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start) + 1, start + count).toString())
                        }
                    }
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private var enabledFlag: Int
    private var isInit: Boolean = false
    private var hashtagListener: ((SociableView, CharSequence) -> Unit)? = null
    private var mentionListener: ((SociableView, CharSequence) -> Unit)? = null
    private var hashtagWatcher: ((SociableView, CharSequence) -> Unit)? = null
    private var mentionWatcher: ((SociableView, CharSequence) -> Unit)? = null
    private var isHashtagEditing: Boolean = false
    private var isMentionEditing: Boolean = false

    override val internalContext: Context = view.context
    override val hashtags: Collection<String> get() = if (!isHashtagEnabled) emptyList() else newList(PATTERN_HASHTAG, view.text)
    override val mentions: Collection<String> get() = if (!isHashtagEnabled) emptyList() else newList(PATTERN_MENTION, view.text)
    override val hyperlinks: Collection<String> get() = if (!isHashtagEnabled) emptyList() else newList(Patterns.WEB_URL, view.text)

    override var isHashtagEnabled: Boolean
        get() = enabledFlag or TYPE_HASHTAG == enabledFlag
        set(value) {
            enabledFlag = if (value)
                enabledFlag or TYPE_HASHTAG
            else
                enabledFlag and TYPE_HASHTAG.inv()
            colorize()
        }

    override var isMentionEnabled: Boolean
        get() = enabledFlag or TYPE_MENTION == enabledFlag
        set(value) {
            enabledFlag = if (value)
                enabledFlag or TYPE_MENTION
            else
                enabledFlag and TYPE_MENTION.inv()
            colorize()
        }

    override var isHyperlinkEnabled: Boolean
        get() = enabledFlag or TYPE_HYPERLINK == enabledFlag
        set(value) {
            enabledFlag = if (value)
                enabledFlag or TYPE_HYPERLINK
            else
                enabledFlag and TYPE_HYPERLINK.inv()
            colorize()
        }

    override var hashtagColor: Int = 0
        get() = field
        set(value) {
            field = value
            if (isInit) colorize()
        }

    override var mentionColor: Int = 0
        get() = field
        set(value) {
            field = value
            if (isInit) colorize()
        }

    override var hyperlinkColor: Int = 0
        get() = field
        set(value) {
            field = value
            if (isInit) colorize()
        }

    init {
        view.addTextChangedListener(mTextWatcher)
        view.setText(view.text, TextView.BufferType.SPANNABLE)
        val a = view.context.obtainStyledAttributes(attrs, R.styleable.SocialView, 0, R.style.Widget_SocialView)
        enabledFlag = a.getInteger(R.styleable.SocialView_socialEnabled, TYPE_HASHTAG or TYPE_MENTION or TYPE_HYPERLINK)
        hashtagColor = a.getColorStateList(R.styleable.SocialView_hashtagColor).defaultColor
        mentionColor = a.getColorStateList(R.styleable.SocialView_mentionColor).defaultColor
        hyperlinkColor = a.getColorStateList(R.styleable.SocialView_hyperlinkColor).defaultColor
        a.recycle()
        colorize()
        isInit = true
    }

    override fun setOnHashtagClickListener(listener: ((SociableView, CharSequence) -> Unit)?) {
        if (view.movementMethod == null || view.movementMethod !is LinkMovementMethod) {
            view.movementMethod = LinkMovementMethod.getInstance()
        }
        hashtagListener = listener
        colorize()
    }

    override fun setOnMentionClickListener(listener: ((SociableView, CharSequence) -> Unit)?) {
        if (view.movementMethod == null || view.movementMethod !is LinkMovementMethod) {
            view.movementMethod = LinkMovementMethod.getInstance()
        }
        mentionListener = listener
        colorize()
    }

    override fun setHashtagTextChangedListener(watcher: ((SociableView, CharSequence) -> Unit)?) {
        hashtagWatcher = watcher
    }

    override fun setMentionTextChangedListener(watcher: ((SociableView, CharSequence) -> Unit)?) {
        mentionWatcher = watcher
    }

    private fun indexOfNextNonLetterDigit(text: CharSequence, start: Int): Int = (start + 1..text.length - 1).firstOrNull { !Character.isLetterOrDigit(text[it]) } ?: text.length

    private fun indexOfPreviousNonLetterDigit(text: CharSequence, start: Int, end: Int): Int = (end downTo start + 1).firstOrNull { !Character.isLetterOrDigit(text[it]) } ?: start

    private fun colorize() {
        val spannable = view.text
        check(spannable is Spannable, { "Attached text is not a Spannable, add TextView.BufferType.SPANNABLE when setting text to this TextView." })
        spannable as Spannable
        spannable.removeSpans(spannable.getSpans(0, spannable.length, CharacterStyle::class.java))
        if (isHashtagEnabled) {
            spannable.putSpansAll(PATTERN_HASHTAG, {
                if (hashtagListener == null) {
                    ForegroundColorSpan(hashtagColor)
                }
                object : ForegroundColorClickableSpan(hashtagColor) {
                    override fun onClick(widget: View) {
                        hashtagListener!!.invoke(view, spannable.subSequence(spannable.getSpanStart(this) + 1, spannable.getSpanEnd(this)))
                    }
                }
            })
        }
        if (isMentionEnabled) {
            spannable.putSpansAll(PATTERN_MENTION, {
                if (mentionListener == null) {
                    ForegroundColorSpan(mentionColor)
                }
                object : ForegroundColorClickableSpan(mentionColor) {
                    override fun onClick(widget: View) {
                        mentionListener!!.invoke(view, spannable.subSequence(spannable.getSpanStart(this) + 1, spannable.getSpanEnd(this)))
                    }
                }
            })
        }
        if (isHyperlinkEnabled) {
            spannable.putSpansAll(Patterns.WEB_URL, { SimpleURLSpan(spannable, hyperlinkColor) })
        }
    }
}