package com.hendraanggrian.widget

import android.content.Context
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView
import android.text.*
import android.util.AttributeSet
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import com.hendraanggrian.socialview.SocialView
import com.hendraanggrian.socialview.SocialViewImpl

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class SocialAutoCompleteTextView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = android.support.v7.appcompat.R.attr.autoCompleteTextViewStyle
) : AppCompatMultiAutoCompleteTextView(context, attrs, defStyleAttr), SocialView {

    private var mImpl = SocialViewImpl(this, attrs)
    private val mTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(editable: Editable?) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (s.isNotEmpty() && start < s.length) {
                when (s[start]) {
                    '#' -> if (adapter !== hashtagAdapter) {
                        setAdapter(hashtagAdapter)
                    }
                    '@' -> if (adapter !== mentionAdapter) {
                        setAdapter(mentionAdapter)
                    }
                }
            }
        }
    }
    private val mEnabledSymbols: CharArray
        get() = ArrayList<Char>().apply {
            if (isHashtagEnabled) {
                add('#')
            }
            if (isMentionEnabled) {
                add('@')
            }
        }.toCharArray()

    var hashtagAdapter: ArrayAdapter<*>? = null
    var mentionAdapter: ArrayAdapter<*>? = null

    init {
        addTextChangedListener(mTextWatcher)
        setTokenizer(SymbolsTokenizer(mEnabledSymbols))
    }

    override var isHashtagEnabled
        get() = mImpl.isHashtagEnabled
        set(value) {
            mImpl.isHashtagEnabled = value
            setTokenizer(SymbolsTokenizer(mEnabledSymbols))
        }
    override var isMentionEnabled
        get() = mImpl.isMentionEnabled
        set(value) {
            mImpl.isMentionEnabled = value
            setTokenizer(SymbolsTokenizer(mEnabledSymbols))
        }
    override var isHyperlinkEnabled
        get() = mImpl.isHyperlinkEnabled
        set(value) {
            mImpl.isHyperlinkEnabled = value
            setTokenizer(SymbolsTokenizer(mEnabledSymbols))
        }

    override var hashtagColor
        get() = mImpl.hashtagColor
        set(value) {
            mImpl.hashtagColor = value
        }
    override var mentionColor
        get() = mImpl.mentionColor
        set(value) {
            mImpl.mentionColor = value
        }
    override var hyperlinkColor
        get() = mImpl.hyperlinkColor
        set(value) {
            mImpl.hyperlinkColor = value
        }

    override fun setOnHashtagClickListener(listener: ((SocialView, CharSequence) -> Unit)?) = mImpl.setOnHashtagClickListener(listener)
    override fun setOnMentionClickListener(listener: ((SocialView, CharSequence) -> Unit)?) = mImpl.setOnMentionClickListener(listener)
    override fun setOnHyperlinkClickListener(listener: ((SocialView, CharSequence) -> Unit)?) = mImpl.setOnHyperlinkClickListener(listener)
    override fun setHashtagTextChangedListener(watcher: ((SocialView, CharSequence) -> Unit)?) = mImpl.setHashtagTextChangedListener(watcher)
    override fun setMentionTextChangedListener(watcher: ((SocialView, CharSequence) -> Unit)?) = mImpl.setMentionTextChangedListener(watcher)

    override fun colorize() = mImpl.colorize()

    /**
     * While [MultiAutoCompleteTextView.CommaTokenizer] tracks only comma symbol,
     * [SymbolsTokenizer] can track multiple characters, in this instance, are hashtag and at symbol.
     */
    class SymbolsTokenizer(private val symbols: CharArray) : MultiAutoCompleteTextView.Tokenizer {

        override fun findTokenStart(text: CharSequence, cursor: Int): Int {
            var i = cursor
            while (i > 0 && !symbols.contains(text[i - 1])) {
                i--
            }
            while (i < cursor && text[i] == ' ') {
                i++
            }
            return i
        }

        override fun findTokenEnd(text: CharSequence, cursor: Int): Int {
            var i = cursor
            val len = text.length
            while (i < len) {
                if (symbols.contains(text[i])) {
                    return i
                } else {
                    i++
                }
            }
            return len
        }

        override fun terminateToken(text: CharSequence): CharSequence {
            var i = text.length
            while (i > 0 && text[i - 1] == ' ') {
                i--
            }
            if (i > 0 && symbols.contains(text[i - 1])) {
                return text
            } else if (text is Spanned) {
                val sp = SpannableString(text.toString() + " ")
                TextUtils.copySpansFrom(text, 0, text.length, Any::class.java, sp, 0)
                return sp
            } else {
                return text.toString() + " "
            }
        }
    }
}