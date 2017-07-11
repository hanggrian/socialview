package com.hendraanggrian.widget

import android.content.Context
import android.text.*
import android.util.AttributeSet
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import com.hendraanggrian.socialview.SociableView
import com.hendraanggrian.socialview.SociableViewImpl

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class SocialAutoCompleteTextView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = android.support.v7.appcompat.R.attr.autoCompleteTextViewStyle) : MultiAutoCompleteTextView(context, attrs, defStyleAttr), SociableView {

    private val impl = SociableViewImpl(this, attrs)
    private val mTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

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

        override fun afterTextChanged(editable: Editable?) {}
    }
    var hashtagAdapter: ArrayAdapter<*>? = null
    var mentionAdapter: ArrayAdapter<*>? = null

    override val internalContext: Context get() = context
    override val hashtags: Collection<String> get() = impl.hashtags
    override val mentions: Collection<String> get() = impl.mentions
    override val hyperlinks: Collection<String> get() = impl.hyperlinks

    init {
        addTextChangedListener(mTextWatcher)
        setTokenizer(SymbolsTokenizer(getEnabledSymbols()))
    }

    override var isHashtagEnabled: Boolean
        get() = impl.isHashtagEnabled
        set(value) {
            impl.isHashtagEnabled = value
            setTokenizer(SymbolsTokenizer(getEnabledSymbols()))
        }
    override var isMentionEnabled: Boolean
        get() = impl.isMentionEnabled
        set(value) {
            impl.isMentionEnabled = value
            setTokenizer(SymbolsTokenizer(getEnabledSymbols()))
        }
    override var isHyperlinkEnabled: Boolean
        get() = impl.isHyperlinkEnabled
        set(value) {
            impl.isHyperlinkEnabled = value
            setTokenizer(SymbolsTokenizer(getEnabledSymbols()))
        }

    override var hashtagColor: Int
        get() = impl.hashtagColor
        set(value) {
            impl.hashtagColor = value
        }
    override var mentionColor: Int
        get() = impl.mentionColor
        set(value) {
            impl.mentionColor = value
        }
    override var hyperlinkColor: Int
        get() = impl.hyperlinkColor
        set(value) {
            impl.hyperlinkColor = value
        }

    override fun setOnHashtagClickListener(listener: ((SociableView, CharSequence) -> Unit)?) = impl.setOnHashtagClickListener(listener)
    override fun setOnMentionClickListener(listener: ((SociableView, CharSequence) -> Unit)?) = impl.setOnMentionClickListener(listener)

    override fun setHashtagTextChangedListener(watcher: ((SociableView, CharSequence) -> Unit)?) = impl.setHashtagTextChangedListener(watcher)
    override fun setMentionTextChangedListener(watcher: ((SociableView, CharSequence) -> Unit)?) = impl.setMentionTextChangedListener(watcher)

    private fun getEnabledSymbols(): Collection<Char> = ArrayList<Char>().apply {
        if (isHashtagEnabled) {
            add('#')
        }
        if (isMentionEnabled) {
            add('@')
        }
    }

    class SymbolsTokenizer(val symbols: Collection<Char>) : MultiAutoCompleteTextView.Tokenizer {
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
            while (i < len)
                if (symbols.contains(text[i]))
                    return i
                else
                    i++
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