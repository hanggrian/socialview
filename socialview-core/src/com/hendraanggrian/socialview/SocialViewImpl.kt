package com.hendraanggrian.socialview

import android.content.res.ColorStateList
import android.text.Editable
import android.text.Spannable
import android.text.Spanned
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.method.LinkMovementMethod.getInstance
import android.text.style.CharacterStyle
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.URLSpan
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import android.widget.TextView.BufferType.SPANNABLE
import com.hendraanggrian.socialview.SocialView.Companion.REGEX_HASHTAG
import com.hendraanggrian.socialview.SocialView.Companion.REGEX_HYPERLINK
import com.hendraanggrian.socialview.SocialView.Companion.REGEX_MENTION

/**
 * Implementation of [SocialView] that is delegated to all socialview's widgets.
 *
 * @see SocialView
 */
class SocialViewImpl : SocialView {

    private val _textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            if (count > 0 && start > 0) when (s[start - 1]) {
                '#' -> {
                    _hashtagEditing = true
                    _mentionEditing = false
                }
                '@' -> {
                    _hashtagEditing = false
                    _mentionEditing = true
                }
                else -> when {
                    !Character.isLetterOrDigit(s[start - 1]) -> {
                        _hashtagEditing = false
                        _mentionEditing = false
                    }
                    _hashtagWatcher != null && _hashtagEditing ->
                        _hashtagWatcher!!(
                            this@SocialViewImpl,
                            s.substring(indexOfPreviousNonLetterDigit(s, 0, start - 1) + 1, start))
                    _mentionWatcher != null && _mentionEditing ->
                        _mentionWatcher!!(
                            this@SocialViewImpl,
                            s.substring(indexOfPreviousNonLetterDigit(s, 0, start - 1) + 1, start))
                }
            }
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            // triggered when text is added
            if (s.isEmpty()) return
            colorize()
            if (start < s.length) {
                if (start + count - 1 < 0) return
                when (s[start + count - 1]) {
                    '#' -> {
                        _hashtagEditing = true
                        _mentionEditing = false
                    }
                    '@' -> {
                        _hashtagEditing = false
                        _mentionEditing = true
                    }
                    else -> when {
                        !Character.isLetterOrDigit(s[start]) -> {
                            _hashtagEditing = false
                            _mentionEditing = false
                        }
                        _hashtagWatcher != null && _hashtagEditing ->
                            _hashtagWatcher!!(
                                this@SocialViewImpl,
                                s.substring(
                                    indexOfPreviousNonLetterDigit(s, 0, start) + 1, start + count))
                        _mentionWatcher != null && _mentionEditing ->
                            _mentionWatcher!!(
                                this@SocialViewImpl,
                                s.substring(
                                    indexOfPreviousNonLetterDigit(s, 0, start) + 1, start + count))
                    }
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private lateinit var _view: TextView
    private var _flags: Int = 0
    private lateinit var _hashtagColor: ColorStateList
    private lateinit var _mentionColor: ColorStateList
    private lateinit var _hyperlinkColor: ColorStateList

    private var _hashtagListener: ((SocialView, String) -> Unit)? = null
    private var _mentionListener: ((SocialView, String) -> Unit)? = null
    private var _hyperlinkListener: ((SocialView, String) -> Unit)? = null
    private var _hashtagWatcher: ((SocialView, String) -> Unit)? = null
    private var _mentionWatcher: ((SocialView, String) -> Unit)? = null
    private var _hashtagEditing: Boolean = false
    private var _mentionEditing: Boolean = false

    /** A constructor replacement, since `this` ([TextView]) cannot be referenced in constructor. */
    fun init(v: TextView, attrs: AttributeSet?) {
        _view = v
        _view.addTextChangedListener(_textWatcher)
        _view.setText(_view.text, SPANNABLE)
        val a = _view.context.obtainStyledAttributes(attrs, R.styleable.SocialView,
            R.attr.socialViewStyle, R.style.Widget_SocialView)
        _flags = a.getInteger(R.styleable.SocialView_socialFlags,
            FLAG_HASHTAG or FLAG_MENTION or FLAG_HYPERLINK)
        _hashtagColor = a.getColorStateList(R.styleable.SocialView_hashtagColor)
        _mentionColor = a.getColorStateList(R.styleable.SocialView_mentionColor)
        _hyperlinkColor = a.getColorStateList(R.styleable.SocialView_hyperlinkColor)
        a.recycle()
        colorize()
    }

    override var isHashtagEnabled: Boolean
        get() = _flags or FLAG_HASHTAG == _flags
        set(enabled) {
            _flags = when {
                enabled -> _flags or FLAG_HASHTAG
                else -> _flags and FLAG_HASHTAG.inv()
            }
            colorize()
        }

    override var isMentionEnabled: Boolean
        get() = _flags or FLAG_MENTION == _flags
        set(enabled) {
            _flags = when {
                enabled -> _flags or FLAG_MENTION
                else -> _flags and FLAG_MENTION.inv()
            }
            colorize()
        }

    override var isHyperlinkEnabled: Boolean
        get() = _flags or FLAG_HYPERLINK == _flags
        set(enabled) {
            _flags = when {
                enabled -> _flags or FLAG_HYPERLINK
                else -> _flags and FLAG_HYPERLINK.inv()
            }
            colorize()
        }

    override var hashtagColor: ColorStateList
        get() = _hashtagColor
        set(colorStateList) {
            _hashtagColor = colorStateList
            colorize()
        }

    override var mentionColor: ColorStateList
        get() = _mentionColor
        set(colorStateList) {
            _mentionColor = colorStateList
            colorize()
        }

    override var hyperlinkColor: ColorStateList
        get() = _hyperlinkColor
        set(colorStateList) {
            _hyperlinkColor = colorStateList
            colorize()
        }

    override fun colorize() {
        val spannable = _view.text
        check(spannable is Spannable, {
            "Attached text is not a Spannable," +
                "add TextView.BufferType.SPANNABLE when setting text to this TextView."
        })
        spannable as Spannable
        spannable.getSpans(0, spannable.length, CharacterStyle::class.java).forEach {
            spannable.removeSpan(it)
        }
        if (isHashtagEnabled) spannable.span(REGEX_HASHTAG, { s ->
            _hashtagListener?.newClickableSpan(s, _hashtagColor)
                ?: ForegroundColorSpan(_hashtagColor.defaultColor)
        })
        if (isMentionEnabled) spannable.span(REGEX_MENTION, { s ->
            _mentionListener?.newClickableSpan(s, _mentionColor)
                ?: ForegroundColorSpan(_mentionColor.defaultColor)
        })
        if (isHyperlinkEnabled) spannable.span(REGEX_HYPERLINK, { s ->
            _hyperlinkListener?.newClickableSpan(s, _hyperlinkColor, true) ?: object : URLSpan(s) {
                override fun updateDrawState(ds: TextPaint) {
                    ds.color = _hyperlinkColor.defaultColor
                    ds.isUnderlineText = true
                }
            }
        })
    }

    override fun setOnHashtagClickListener(listener: ((view: SocialView, String) -> Unit)?) {
        _view.setLinkMovementMethodIfNotAlready()
        _hashtagListener = listener
        colorize()
    }

    override fun setOnMentionClickListener(listener: ((view: SocialView, String) -> Unit)?) {
        _view.setLinkMovementMethodIfNotAlready()
        _mentionListener = listener
        colorize()
    }

    override fun setOnHyperlinkClickListener(listener: ((view: SocialView, String) -> Unit)?) {
        _view.setLinkMovementMethodIfNotAlready()
        _hyperlinkListener = listener
        colorize()
    }

    override fun setHashtagTextChangedListener(watcher: ((view: SocialView, String) -> Unit)?) {
        _hashtagWatcher = watcher
    }

    override fun setMentionTextChangedListener(watcher: ((view: SocialView, String) -> Unit)?) {
        _mentionWatcher = watcher
    }

    override val hashtags: List<String>
        get() = if (!isHashtagEnabled) emptyList() else REGEX_HASHTAG.newList

    override val mentions: List<String>
        get() = if (!isMentionEnabled) emptyList() else REGEX_MENTION.newList

    override val hyperlinks: List<String>
        get() = if (!isHyperlinkEnabled) emptyList() else REGEX_HYPERLINK.newList

    private fun indexOfNextNonLetterDigit(
        text: CharSequence,
        start: Int
    ): Int = (start + 1 until text.length).firstOrNull { !Character.isLetterOrDigit(text[it]) }
        ?: text.length

    private fun indexOfPreviousNonLetterDigit(
        text: CharSequence,
        start: Int,
        end: Int
    ): Int = (end downTo start + 1).firstOrNull { !Character.isLetterOrDigit(text[it]) } ?: start

    private fun TextView.setLinkMovementMethodIfNotAlready() {
        if (movementMethod == null || movementMethod !is LinkMovementMethod)
            movementMethod = getInstance()
    }

    private fun ((SocialView, String) -> Unit).newClickableSpan(
        s: String,
        color: ColorStateList,
        underline: Boolean = false
    ): CharacterStyle = object : ClickableSpan() {
        override fun onClick(widget: View) = invoke(this@SocialViewImpl, when {
            this@newClickableSpan !== _hyperlinkListener -> s.substring(1)
            else -> s
        })

        override fun updateDrawState(ds: TextPaint) {
            ds.color = color.defaultColor
            ds.isUnderlineText = underline
        }
    }

    private val Regex.newList: List<String>
        get() {
            val list = ArrayList<String>()
            val matcher = toPattern().matcher(_view.text)
            while (matcher.find()) list.add(matcher.group(when {
                this !== REGEX_HYPERLINK -> 1 /* remove hashtag and mention symbol */
                else -> 0
            }))
            return list
        }

    private companion object {
        const val FLAG_HASHTAG: Int = 1
        const val FLAG_MENTION: Int = 2
        const val FLAG_HYPERLINK: Int = 4

        @Suppress("NOTHING_TO_INLINE")
        inline fun Spannable.span(
            regex: Regex,
            vararg spans: (String) -> Any,
            flags: Int = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        ) {
            val matcher = regex.toPattern().matcher(this)
            while (matcher.find()) {
                val start = matcher.start()
                val end = matcher.end()
                for (span in spans) setSpan(span(substring(start, end)), start, end, flags)
            }
        }
    }
}