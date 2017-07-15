package com.hendraanggrian.socialview

import android.text.Editable
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.widget.TextView

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class SocialViewImpl<out T>(
        override val view: T, // original View
        attrs: AttributeSet? = null // styles and shit
) : SocialView where T : TextView, T : SocialView {

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
                        else -> if (!Character.isLetterOrDigit(s[start])) {
                            isHashtagEditing = false
                            isMentionEditing = false
                        } else if (hashtagWatcher != null && isHashtagEditing) {
                            hashtagWatcher!!.invoke(this@SocialViewImpl, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start) + 1, start + count).toString())
                        } else if (mentionWatcher != null && isMentionEditing) {
                            mentionWatcher!!.invoke(this@SocialViewImpl, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start) + 1, start + count).toString())
                        }
                    }
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private var enabledFlag: Int
    private var hashtagListener: ((SocialView, CharSequence) -> Unit)? = null
    private var mentionListener: ((SocialView, CharSequence) -> Unit)? = null
    private var hashtagWatcher: ((SocialView, CharSequence) -> Unit)? = null
    private var mentionWatcher: ((SocialView, CharSequence) -> Unit)? = null
    private var isInit = false
    private var isHashtagEditing = false
    private var isMentionEditing = false

    override var isHashtagEnabled: Boolean
        get() = enabledFlag or SocialView.FLAG_HASHTAG == enabledFlag
        set(value) {
            enabledFlag = if (value)
                enabledFlag or SocialView.FLAG_HASHTAG
            else
                enabledFlag and SocialView.FLAG_HASHTAG.inv()
            colorize()
        }

    override var isMentionEnabled: Boolean
        get() = enabledFlag or SocialView.FLAG_MENTION == enabledFlag
        set(value) {
            enabledFlag = if (value)
                enabledFlag or SocialView.FLAG_MENTION
            else
                enabledFlag and SocialView.FLAG_MENTION.inv()
            colorize()
        }

    override var isHyperlinkEnabled: Boolean
        get() = enabledFlag or SocialView.FLAG_HYPERLINK == enabledFlag
        set(value) {
            enabledFlag = if (value)
                enabledFlag or SocialView.FLAG_HYPERLINK
            else
                enabledFlag and SocialView.FLAG_HYPERLINK.inv()
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
        enabledFlag = a.getInteger(R.styleable.SocialView_socialEnabled, SocialView.FLAG_HASHTAG or SocialView.FLAG_MENTION or SocialView.FLAG_HYPERLINK)
        hashtagColor = a.getColorStateList(R.styleable.SocialView_hashtagColor).defaultColor
        mentionColor = a.getColorStateList(R.styleable.SocialView_mentionColor).defaultColor
        hyperlinkColor = a.getColorStateList(R.styleable.SocialView_hyperlinkColor).defaultColor
        a.recycle()
        colorize()
        isInit = true
    }

    override fun getOnHashtagClickListener(): ((SocialView, CharSequence) -> Unit)? = hashtagListener
    override fun getOnMentionClickListener(): ((SocialView, CharSequence) -> Unit)? = mentionListener

    override fun setOnHashtagClickListener(listener: ((SocialView, CharSequence) -> Unit)?) {
        if (view.movementMethod == null || view.movementMethod !is LinkMovementMethod) {
            view.movementMethod = LinkMovementMethod.getInstance()
        }
        hashtagListener = listener
        colorize()
    }

    override fun setOnMentionClickListener(listener: ((SocialView, CharSequence) -> Unit)?) {
        if (view.movementMethod == null || view.movementMethod !is LinkMovementMethod) {
            view.movementMethod = LinkMovementMethod.getInstance()
        }
        mentionListener = listener
        colorize()
    }

    override fun setHashtagTextChangedListener(watcher: ((SocialView, CharSequence) -> Unit)?) {
        hashtagWatcher = watcher
    }

    override fun setMentionTextChangedListener(watcher: ((SocialView, CharSequence) -> Unit)?) {
        mentionWatcher = watcher
    }

    private fun indexOfNextNonLetterDigit(text: CharSequence, start: Int): Int = (start + 1..text.length - 1).firstOrNull { !Character.isLetterOrDigit(text[it]) } ?: text.length

    private fun indexOfPreviousNonLetterDigit(text: CharSequence, start: Int, end: Int): Int = (end downTo start + 1).firstOrNull { !Character.isLetterOrDigit(text[it]) } ?: start
}