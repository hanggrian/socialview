package com.hendraanggrian.socialview

import android.content.res.ColorStateList
import android.text.Editable
import android.text.Spannable
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

    companion object {
        private const val FLAG_HASHTAG: Int = 1
        private const val FLAG_MENTION: Int = 2
        private const val FLAG_HYPERLINK: Int = 4
    }

    private val mTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            if (count > 0 && start > 0) when (s[start - 1]) {
                '#' -> {
                    mHashtagEditing = true
                    mMentionEditing = false
                }
                '@' -> {
                    mHashtagEditing = false
                    mMentionEditing = true
                }
                else -> when {
                    !Character.isLetterOrDigit(s[start - 1]) -> {
                        mHashtagEditing = false
                        mMentionEditing = false
                    }
                    mHashtagWatcher != null && mHashtagEditing -> mHashtagWatcher!!.invoke(this@SocialViewImpl, s.substring(indexOfPreviousNonLetterDigit(s, 0, start - 1) + 1, start))
                    mMentionWatcher != null && mMentionEditing -> mMentionWatcher!!.invoke(this@SocialViewImpl, s.substring(indexOfPreviousNonLetterDigit(s, 0, start - 1) + 1, start))
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
                        mHashtagEditing = true
                        mMentionEditing = false
                    }
                    '@' -> {
                        mHashtagEditing = false
                        mMentionEditing = true
                    }
                    else -> when {
                        !Character.isLetterOrDigit(s[start]) -> {
                            mHashtagEditing = false
                            mMentionEditing = false
                        }
                        mHashtagWatcher != null && mHashtagEditing -> mHashtagWatcher!!.invoke(this@SocialViewImpl, s.substring(indexOfPreviousNonLetterDigit(s, 0, start) + 1, start + count))
                        mMentionWatcher != null && mMentionEditing -> mMentionWatcher!!.invoke(this@SocialViewImpl, s.substring(indexOfPreviousNonLetterDigit(s, 0, start) + 1, start + count))
                    }
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private lateinit var view: TextView
    private var mFlags: Int = 0
    private lateinit var mHashtagColor: ColorStateList
    private lateinit var mMentionColor: ColorStateList
    private lateinit var mHyperlinkColor: ColorStateList

    private var mHashtagListener: ((SocialView, String) -> Unit)? = null
    private var mMentionListener: ((SocialView, String) -> Unit)? = null
    private var mHyperlinkListener: ((SocialView, String) -> Unit)? = null
    private var mHashtagWatcher: ((SocialView, String) -> Unit)? = null
    private var mMentionWatcher: ((SocialView, String) -> Unit)? = null
    private var mHashtagEditing: Boolean = false
    private var mMentionEditing: Boolean = false

    /** A constructor replacement, since `this` ([TextView]) cannot be referenced in constructor. */
    fun init(v: TextView, attrs: AttributeSet?) {
        view = v
        view.addTextChangedListener(mTextWatcher)
        view.setText(view.text, SPANNABLE)
        val a = view.context.obtainStyledAttributes(attrs, R.styleable.SocialView, R.attr.socialViewStyle, R.style.Widget_SocialView)
        mFlags = a.getInteger(R.styleable.SocialView_socialFlags, FLAG_HASHTAG or FLAG_MENTION or FLAG_HYPERLINK)
        mHashtagColor = a.getColorStateList(R.styleable.SocialView_hashtagColor)
        mMentionColor = a.getColorStateList(R.styleable.SocialView_mentionColor)
        mHyperlinkColor = a.getColorStateList(R.styleable.SocialView_hyperlinkColor)
        a.recycle()
        colorize()
    }

    override var isHashtagEnabled: Boolean
        get() = mFlags or FLAG_HASHTAG == mFlags
        set(enabled) {
            mFlags = when {
                enabled -> mFlags or FLAG_HASHTAG
                else -> mFlags and FLAG_HASHTAG.inv()
            }
            colorize()
        }

    override var isMentionEnabled: Boolean
        get() = mFlags or FLAG_MENTION == mFlags
        set(enabled) {
            mFlags = when {
                enabled -> mFlags or FLAG_MENTION
                else -> mFlags and FLAG_MENTION.inv()
            }
            colorize()
        }

    override var isHyperlinkEnabled: Boolean
        get() = mFlags or FLAG_HYPERLINK == mFlags
        set(enabled) {
            mFlags = when {
                enabled -> mFlags or FLAG_HYPERLINK
                else -> mFlags and FLAG_HYPERLINK.inv()
            }
            colorize()
        }

    override var hashtagColor: ColorStateList
        get() = mHashtagColor
        set(colorStateList) {
            mHashtagColor = colorStateList
            colorize()
        }

    override var mentionColor: ColorStateList
        get() = mMentionColor
        set(colorStateList) {
            mMentionColor = colorStateList
            colorize()
        }

    override var hyperlinkColor: ColorStateList
        get() = mHyperlinkColor
        set(colorStateList) {
            mHyperlinkColor = colorStateList
            colorize()
        }

    override fun colorize() {
        val spannable = view.text
        check(spannable is Spannable, { "Attached text is not a Spannable, add TextView.BufferType.SPANNABLE when setting text to this TextView." })
        spannable as Spannable
        spannable.removeSpans(*spannable.getSpans(CharacterStyle::class.java))
        if (isHashtagEnabled) spannable.span(REGEX_HASHTAG, { s ->
            mHashtagListener?.newClickableSpan(s, mHashtagColor) ?: ForegroundColorSpan(mHashtagColor.defaultColor)
        })
        if (isMentionEnabled) spannable.span(REGEX_MENTION, { s ->
            mMentionListener?.newClickableSpan(s, mMentionColor) ?: ForegroundColorSpan(mMentionColor.defaultColor)
        })
        if (isHyperlinkEnabled) spannable.span(REGEX_HYPERLINK, { s ->
            mHyperlinkListener?.newClickableSpan(s, mHyperlinkColor, true) ?: object : URLSpan(s) {
                override fun updateDrawState(ds: TextPaint) {
                    ds.color = mHyperlinkColor.defaultColor
                    ds.isUnderlineText = true
                }
            }
        })
    }

    override fun setOnHashtagClickListener(listener: ((view: SocialView, String) -> Unit)?) {
        view.setLinkMovementMethodIfNotAlready()
        mHashtagListener = listener
        colorize()
    }

    override fun setOnMentionClickListener(listener: ((view: SocialView, String) -> Unit)?) {
        view.setLinkMovementMethodIfNotAlready()
        mMentionListener = listener
        colorize()
    }

    override fun setOnHyperlinkClickListener(listener: ((view: SocialView, String) -> Unit)?) {
        view.setLinkMovementMethodIfNotAlready()
        mHyperlinkListener = listener
        colorize()
    }

    override fun setHashtagTextChangedListener(watcher: ((view: SocialView, String) -> Unit)?) {
        mHashtagWatcher = watcher
    }

    override fun setMentionTextChangedListener(watcher: ((view: SocialView, String) -> Unit)?) {
        mMentionWatcher = watcher
    }

    override val hashtags: List<String> get() = if (!isHashtagEnabled) emptyList() else REGEX_HASHTAG.newList

    override val mentions: List<String> get() = if (!isMentionEnabled) emptyList() else REGEX_MENTION.newList

    override val hyperlinks: List<String> get() = if (!isHyperlinkEnabled) emptyList() else REGEX_HYPERLINK.newList

    private fun indexOfNextNonLetterDigit(text: CharSequence, start: Int): Int = (start + 1 until text.length).firstOrNull { !Character.isLetterOrDigit(text[it]) } ?: text.length

    private fun indexOfPreviousNonLetterDigit(text: CharSequence, start: Int, end: Int): Int = (end downTo start + 1).firstOrNull { !Character.isLetterOrDigit(text[it]) } ?: start

    private fun TextView.setLinkMovementMethodIfNotAlready() {
        if (movementMethod == null || movementMethod !is LinkMovementMethod) movementMethod = getInstance()
    }

    private fun ((SocialView, String) -> Unit).newClickableSpan(s: String, color: ColorStateList, underline: Boolean = false): CharacterStyle = object : ClickableSpan() {
        override fun onClick(widget: View) = invoke(this@SocialViewImpl, if (this@newClickableSpan !== mHyperlinkListener) s.substring(1) else s)
        override fun updateDrawState(ds: TextPaint) {
            ds.color = color.defaultColor
            ds.isUnderlineText = underline
        }
    }

    private val Regex.newList: List<String>
        get() {
            val list = ArrayList<String>()
            val matcher = toPattern().matcher(view.text)
            while (matcher.find()) list.add(matcher.group(if (this !== REGEX_HYPERLINK) 1 /* remove hashtag and mention symbol */ else 0))
            return list
        }
}