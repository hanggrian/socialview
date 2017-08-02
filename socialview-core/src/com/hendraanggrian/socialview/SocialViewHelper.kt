@file:JvmName("SocialViewHelper")

package com.hendraanggrian.socialview

import android.os.Parcel
import android.support.annotation.ColorInt
import android.text.Editable
import android.text.Spannable
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.method.MovementMethod
import android.text.style.CharacterStyle
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.URLSpan
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.hendraanggrian.kota.text.putSpansAll
import com.hendraanggrian.kota.text.removeAllSpans

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class SocialViewHelper private constructor(
        override val view: TextView,
        attrs: AttributeSet? = null) :
        SocialView {

    private val lastMovementMethod: MovementMethod? = view.movementMethod
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
                        mHashtagWatcher!!.invoke(this@SocialViewHelper, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start - 1) + 1, start).toString())
                    } else if (mMentionWatcher != null && mMentionEditing) {
                        mMentionWatcher!!.invoke(this@SocialViewHelper, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start - 1) + 1, start).toString())
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
                        mHashtagWatcher!!.invoke(this@SocialViewHelper, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start) + 1, start + count).toString())
                    } else if (mMentionWatcher != null && mMentionEditing) {
                        mMentionWatcher!!.invoke(this@SocialViewHelper, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start) + 1, start + count).toString())
                    }
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private var flags: Int
    private var mHashtagListener: ((SocialView, CharSequence) -> Unit)? = null
    private var mMentionListener: ((SocialView, CharSequence) -> Unit)? = null
    private var mHashtagWatcher: ((SocialView, CharSequence) -> Unit)? = null
    private var mMentionWatcher: ((SocialView, CharSequence) -> Unit)? = null
    @ColorInt private var mHashtagColor: Int
    @ColorInt private var mMentionColor: Int
    @ColorInt private var mHyperlinkColor: Int
    private var mHashtagEditing = false
    private var mMentionEditing = false

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

    init {
        view.addTextChangedListener(mTextWatcher)
        view.setText(view.text, TextView.BufferType.SPANNABLE)
        val a = view.context.obtainStyledAttributes(attrs, R.styleable.SocialView, R.attr.socialViewStyle, R.style.Widget_SocialView)
        flags = a.getInteger(R.styleable.SocialView_socialFlags, SocialView.FLAG_HASHTAG or SocialView.FLAG_MENTION or SocialView.FLAG_HYPERLINK)
        mHashtagColor = a.getColorStateList(R.styleable.SocialView_hashtagColor).defaultColor
        mMentionColor = a.getColorStateList(R.styleable.SocialView_mentionColor).defaultColor
        mHyperlinkColor = a.getColorStateList(R.styleable.SocialView_hyperlinkColor).defaultColor
        a.recycle()
        colorize()
    }

    override fun colorize() {
        val spannable = view.text
        check(spannable is Spannable, { "Attached text is not a Spannable, add TextView.BufferType.SPANNABLE when setting text to this TextView." })
        spannable as Spannable
        spannable.removeAllSpans(CharacterStyle::class.java)
        if (isHashtagEnabled) {
            mHashtagListener.let {
                spannable.putSpansAll(SocialView.HASHTAG_PATTERN, {
                    if (it == null) {
                        ForegroundColorSpan(hashtagColor)
                    } else {
                        object : ClickableSpan() {
                            override fun onClick(widget: View) = it.invoke(this@SocialViewHelper, spannable.subSequence(spannable.getSpanStart(this) + 1, spannable.getSpanEnd(this)))
                            override fun updateDrawState(ds: TextPaint) {
                                ds.color = hashtagColor
                            }
                        }
                    }
                })
            }
        }
        if (isMentionEnabled) {
            mMentionListener.let {
                spannable.putSpansAll(SocialView.MENTION_PATTERN, {
                    if (it == null) {
                        ForegroundColorSpan(mentionColor)
                    } else {
                        object : ClickableSpan() {
                            override fun onClick(widget: View) = it.invoke(this@SocialViewHelper, spannable.subSequence(spannable.getSpanStart(this) + 1, spannable.getSpanEnd(this)))
                            override fun updateDrawState(ds: TextPaint) {
                                ds.color = mentionColor
                            }
                        }
                    }
                })
            }
        }
        if (isHyperlinkEnabled) {
            spannable.putSpansAll(SocialView.HYPERLINK_PATTERN, {
                object : URLSpan(null as String?) {
                    override fun writeToParcel(dest: Parcel, flags: Int) = dest.writeString(url)
                    override fun getURL() = spannable.subSequence(spannable.getSpanStart(this), spannable.getSpanEnd(this)).toString()
                    override fun updateDrawState(ds: TextPaint) {
                        ds.linkColor = hyperlinkColor
                        super.updateDrawState(ds)
                    }
                }
            })
        }
    }

    override fun detach() {
        view.movementMethod = lastMovementMethod
        view.removeTextChangedListener(mTextWatcher)
    }

    override fun setOnHashtagClickListener(listener: ((SocialView, CharSequence) -> Unit)?) {
        if (view.movementMethod == null || view.movementMethod !is LinkMovementMethod) {
            view.movementMethod = LinkMovementMethod.getInstance()
        }
        mHashtagListener = listener
        colorize()
    }

    override fun setOnMentionClickListener(listener: ((SocialView, CharSequence) -> Unit)?) {
        if (view.movementMethod == null || view.movementMethod !is LinkMovementMethod) {
            view.movementMethod = LinkMovementMethod.getInstance()
        }
        mMentionListener = listener
        colorize()
    }

    override fun setHashtagTextChangedListener(watcher: ((SocialView, CharSequence) -> Unit)?) {
        mHashtagWatcher = watcher
    }

    override fun setMentionTextChangedListener(watcher: ((SocialView, CharSequence) -> Unit)?) {
        mMentionWatcher = watcher
    }

    private fun indexOfNextNonLetterDigit(text: CharSequence, start: Int): Int = (start + 1..text.length - 1).firstOrNull { !Character.isLetterOrDigit(text[it]) } ?: text.length

    private fun indexOfPreviousNonLetterDigit(text: CharSequence, start: Int, end: Int): Int = (end downTo start + 1).firstOrNull { !Character.isLetterOrDigit(text[it]) } ?: start

    companion object {
        @JvmOverloads
        fun attach(view: TextView, attrs: AttributeSet? = null): SocialView = SocialViewHelper(view, attrs)
    }
}