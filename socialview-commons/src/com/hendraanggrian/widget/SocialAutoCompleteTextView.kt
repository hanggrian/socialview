package com.hendraanggrian.widget

import android.content.Context
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView
import android.text.*
import android.util.AttributeSet
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import com.hendraanggrian.socialview.SocialView
import com.hendraanggrian.socialview.SocialViewHelper

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class SocialAutoCompleteTextView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = android.support.v7.appcompat.R.attr.autoCompleteTextViewStyle) : AppCompatMultiAutoCompleteTextView(context, attrs, defStyleAttr), SocialView {

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

    var hashtagAdapter: ArrayAdapter<*>? = null
    var mentionAdapter: ArrayAdapter<*>? = null

    private val helper = SocialViewHelper.attach(this, attrs)
    override val view = this

    init {
        addTextChangedListener(mTextWatcher)
        setTokenizer(SymbolsTokenizer(getEnabledSymbols()))
    }

    override var isHashtagEnabled
        get() = helper.isHashtagEnabled
        set(value) {
            helper.isHashtagEnabled = value
            setTokenizer(SymbolsTokenizer(getEnabledSymbols()))
        }
    override var isMentionEnabled
        get() = helper.isMentionEnabled
        set(value) {
            helper.isMentionEnabled = value
            setTokenizer(SymbolsTokenizer(getEnabledSymbols()))
        }
    override var isHyperlinkEnabled
        get() = helper.isHyperlinkEnabled
        set(value) {
            helper.isHyperlinkEnabled = value
            setTokenizer(SymbolsTokenizer(getEnabledSymbols()))
        }

    override var hashtagColor
        get() = helper.hashtagColor
        set(value) {
            helper.hashtagColor = value
        }
    override var mentionColor
        get() = helper.mentionColor
        set(value) {
            helper.mentionColor = value
        }
    override var hyperlinkColor
        get() = helper.hyperlinkColor
        set(value) {
            helper.hyperlinkColor = value
        }

    override fun getOnHashtagClickListener(): ((SocialView, CharSequence) -> Unit)? = helper.getOnHashtagClickListener()
    override fun getOnMentionClickListener(): ((SocialView, CharSequence) -> Unit)? = helper.getOnMentionClickListener()
    override fun setOnHashtagClickListener(listener: ((SocialView, CharSequence) -> Unit)?) = helper.setOnHashtagClickListener(listener)
    override fun setOnMentionClickListener(listener: ((SocialView, CharSequence) -> Unit)?) = helper.setOnMentionClickListener(listener)
    override fun setHashtagTextChangedListener(watcher: ((SocialView, CharSequence) -> Unit)?) = helper.setHashtagTextChangedListener(watcher)
    override fun setMentionTextChangedListener(watcher: ((SocialView, CharSequence) -> Unit)?) = helper.setMentionTextChangedListener(watcher)

    override fun detach() {
        helper.detach()
        removeTextChangedListener(mTextWatcher)
        setTokenizer(null)
    }

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