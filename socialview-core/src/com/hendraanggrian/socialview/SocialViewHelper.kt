package com.hendraanggrian.socialview

import android.text.Editable
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.method.MovementMethod
import android.util.AttributeSet
import android.widget.TextView

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
                        hashtagWatcher!!.invoke(this@SocialViewHelper, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start - 1) + 1, start).toString())
                    } else if (mentionWatcher != null && isMentionEditing) {
                        mentionWatcher!!.invoke(this@SocialViewHelper, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start - 1) + 1, start).toString())
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
                        hashtagWatcher!!.invoke(this@SocialViewHelper, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start) + 1, start + count).toString())
                    } else if (mentionWatcher != null && isMentionEditing) {
                        mentionWatcher!!.invoke(this@SocialViewHelper, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start) + 1, start + count).toString())
                    }
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private var flags: Int
    private var hashtagListener: ((SocialView, CharSequence) -> Unit)? = null
    private var mentionListener: ((SocialView, CharSequence) -> Unit)? = null
    private var hashtagWatcher: ((SocialView, CharSequence) -> Unit)? = null
    private var mentionWatcher: ((SocialView, CharSequence) -> Unit)? = null
    private var isInit = false
    private var isHashtagEditing = false
    private var isMentionEditing = false

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

    override var hashtagColor = 0
        get() = field
        set(value) {
            field = value
            if (isInit) colorize()
        }

    override var mentionColor = 0
        get() = field
        set(value) {
            field = value
            if (isInit) colorize()
        }

    override var hyperlinkColor = 0
        get() = field
        set(value) {
            field = value
            if (isInit) colorize()
        }

    init {
        view.addTextChangedListener(mTextWatcher)
        view.setText(view.text, TextView.BufferType.SPANNABLE)
        val a = view.context.obtainStyledAttributes(attrs, R.styleable.SocialView, R.attr.socialViewStyle, R.style.Widget_SocialView)
        flags = a.getInteger(R.styleable.SocialView_socialFlags, SocialView.FLAG_HASHTAG or SocialView.FLAG_MENTION or SocialView.FLAG_HYPERLINK)
        hashtagColor = a.getColorStateList(R.styleable.SocialView_hashtagColor).defaultColor
        mentionColor = a.getColorStateList(R.styleable.SocialView_mentionColor).defaultColor
        hyperlinkColor = a.getColorStateList(R.styleable.SocialView_hyperlinkColor).defaultColor
        a.recycle()
        colorize()
        isInit = true
    }

    override fun detach() {
        view.movementMethod = lastMovementMethod
        view.removeTextChangedListener(mTextWatcher)
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

    companion object {
        @JvmOverloads
        fun attach(view: TextView, attrs: AttributeSet? = null): SocialView = SocialViewHelper(view, attrs)
    }
}