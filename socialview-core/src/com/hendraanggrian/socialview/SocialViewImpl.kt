@file:JvmName("SocialViewHelper")

package com.hendraanggrian.socialview

import android.content.res.ColorStateList
import android.text.Editable
import android.text.Spannable
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.CharacterStyle
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.hendraanggrian.common.content.getColorStateListNotNull
import com.hendraanggrian.common.content.openTypedArray
import com.hendraanggrian.common.text.putSpansAll
import com.hendraanggrian.common.text.removeSpans

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class SocialViewImpl constructor(
        val view: TextView,
        attrs: AttributeSet? = null
) : SocialView {

    private val mTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            if (count > 0 && start > 0) {
                when (s[start - 1]) {
                    '#' -> {
                        mHashtagEditing = true
                        mMentionEditing = false
                    }
                    '@' -> {
                        mHashtagEditing = false
                        mMentionEditing = true
                    }
                    else -> if (!Character.isLetterOrDigit(s[start - 1])) {
                        mHashtagEditing = false
                        mMentionEditing = false
                    } else if (mHashtagWatcher != null && mHashtagEditing) {
                        mHashtagWatcher!!.invoke(this@SocialViewImpl, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start - 1) + 1, start).toString())
                    } else if (mMentionWatcher != null && mMentionEditing) {
                        mMentionWatcher!!.invoke(this@SocialViewImpl, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start - 1) + 1, start).toString())
                    }
                }
            }
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            // triggered when text is added
            if (s.isEmpty()) return
            colorize()
            if (start < s.length) {
                if (start + count - 1 < 0) {
                    return
                }
                when (s[start + count - 1]) {
                    '#' -> {
                        mHashtagEditing = true
                        mMentionEditing = false
                    }
                    '@' -> {
                        mHashtagEditing = false
                        mMentionEditing = true
                    }
                    else -> if (!Character.isLetterOrDigit(s[start])) {
                        mHashtagEditing = false
                        mMentionEditing = false
                    } else if (mHashtagWatcher != null && mHashtagEditing) {
                        mHashtagWatcher!!.invoke(this@SocialViewImpl, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start) + 1, start + count).toString())
                    } else if (mMentionWatcher != null && mMentionEditing) {
                        mMentionWatcher!!.invoke(this@SocialViewImpl, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start) + 1, start + count).toString())
                    }
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private var flags = Int.MIN_VALUE
    private lateinit var mHashtagColor: ColorStateList
    private lateinit var mMentionColor: ColorStateList
    private lateinit var mHyperlinkColor: ColorStateList
    private var mHashtagListener: ((SocialView, CharSequence) -> Unit)? = null
    private var mMentionListener: ((SocialView, CharSequence) -> Unit)? = null
    private var mHyperlinkListener: ((SocialView, CharSequence) -> Unit)? = null
    private var mHashtagWatcher: ((SocialView, CharSequence) -> Unit)? = null
    private var mMentionWatcher: ((SocialView, CharSequence) -> Unit)? = null
    private var mHashtagEditing = false
    private var mMentionEditing = false

    init {
        view.addTextChangedListener(mTextWatcher)
        view.setText(view.text, TextView.BufferType.SPANNABLE)
        view.context.openTypedArray(attrs, R.styleable.SocialView, R.attr.socialViewStyle, R.style.Widget_SocialView) {
            flags = getInteger(R.styleable.SocialView_socialFlags, SocialView.FLAG_HASHTAG or SocialView.FLAG_MENTION or SocialView.FLAG_HYPERLINK)
            mHashtagColor = getColorStateListNotNull(R.styleable.SocialView_hashtagColor)
            mMentionColor = getColorStateListNotNull(R.styleable.SocialView_mentionColor)
            mHyperlinkColor = getColorStateListNotNull(R.styleable.SocialView_hyperlinkColor)
        }
        colorize()
    }

    override fun getContext() = view.context!!
    override fun getText() = view.text!!

    override var isHashtagEnabled
        get() = flags or SocialView.FLAG_HASHTAG == flags
        set(value) {
            flags = if (value)
                flags or SocialView.FLAG_HASHTAG
            else
                flags and SocialView.FLAG_HASHTAG.inv()
            colorize()
        }

    override var isMentionEnabled
        get() = flags or SocialView.FLAG_MENTION == flags
        set(value) {
            flags = if (value)
                flags or SocialView.FLAG_MENTION
            else
                flags and SocialView.FLAG_MENTION.inv()
            colorize()
        }

    override var isHyperlinkEnabled
        get() = flags or SocialView.FLAG_HYPERLINK == flags
        set(value) {
            flags = if (value)
                flags or SocialView.FLAG_HYPERLINK
            else
                flags and SocialView.FLAG_HYPERLINK.inv()
            colorize()
        }

    override var hashtagColor
        get() = mHashtagColor
        set(value) {
            mHashtagColor = value
            colorize()
        }

    override var mentionColor
        get() = mMentionColor
        set(value) {
            mMentionColor = value
            colorize()
        }

    override var hyperlinkColor
        get() = mHyperlinkColor
        set(value) {
            mHyperlinkColor = value
            colorize()
        }

    override fun colorize() {
        val spannable = view.text
        check(spannable is Spannable, { "Attached text is not a Spannable, add TextView.BufferType.SPANNABLE when setting text to this TextView." })
        spannable as Spannable
        spannable.removeSpans(CharacterStyle::class.java)
        if (isHashtagEnabled) {
            spannable.putSpansAll(SocialView.HASHTAG_PATTERN, {
                mHashtagListener?.newClickableSpan(spannable, mHashtagColor) ?: ForegroundColorSpan(mHashtagColor.defaultColor)
            })
        }
        if (isMentionEnabled) {
            spannable.putSpansAll(SocialView.MENTION_PATTERN, {
                mMentionListener?.newClickableSpan(spannable, mMentionColor) ?: ForegroundColorSpan(mMentionColor.defaultColor)
            })
        }
        if (isHyperlinkEnabled) {
            spannable.putSpansAll(SocialView.HYPERLINK_PATTERN, {
                mHyperlinkListener?.newClickableSpan(spannable, mHyperlinkColor, true) ?: object : ForegroundColorSpan(mMentionColor.defaultColor) {
                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = true
                    }
                }
            })
        }
    }

    override fun setOnHashtagClickListener(listener: ((SocialView, CharSequence) -> Unit)?) {
        view.setLinkMovementMethodIfNotAlready()
        mHashtagListener = listener
        colorize()
    }

    override fun setOnMentionClickListener(listener: ((SocialView, CharSequence) -> Unit)?) {
        view.setLinkMovementMethodIfNotAlready()
        mMentionListener = listener
        colorize()
    }

    override fun setOnHyperlinkClickListener(listener: ((SocialView, CharSequence) -> Unit)?) {
        view.setLinkMovementMethodIfNotAlready()
        mHyperlinkListener = listener
        colorize()
    }

    override fun setHashtagTextChangedListener(watcher: ((SocialView, CharSequence) -> Unit)?) {
        mHashtagWatcher = watcher
    }

    override fun setMentionTextChangedListener(watcher: ((SocialView, CharSequence) -> Unit)?) {
        mMentionWatcher = watcher
    }

    private fun indexOfNextNonLetterDigit(text: CharSequence, start: Int): Int = (start + 1 until text.length).firstOrNull { !Character.isLetterOrDigit(text[it]) } ?: text.length

    private fun indexOfPreviousNonLetterDigit(text: CharSequence, start: Int, end: Int): Int = (end downTo start + 1).firstOrNull { !Character.isLetterOrDigit(text[it]) } ?: start

    private fun TextView.setLinkMovementMethodIfNotAlready() {
        if (movementMethod == null || movementMethod !is LinkMovementMethod) {
            movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun ((SocialView, CharSequence) -> Unit).newClickableSpan(
            spannable: Spannable,
            color: ColorStateList,
            underline: Boolean = false
    ): CharacterStyle = object : ClickableSpan() {
        override fun onClick(widget: View) = invoke(this@SocialViewImpl, spannable.subSequence(spannable.getSpanStart(this), spannable.getSpanEnd(this)))
        override fun updateDrawState(ds: TextPaint) {
            ds.color = color.defaultColor
            ds.isUnderlineText = underline
        }
    }
}