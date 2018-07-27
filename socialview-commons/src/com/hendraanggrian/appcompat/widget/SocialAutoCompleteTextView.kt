package com.hendraanggrian.appcompat.widget

import android.content.Context
import android.graphics.Rect
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils.copySpansFrom
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import androidx.annotation.IntRange
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView
import com.hendraanggrian.appcompat.internal.SocialViewImpl

/**
 * [android.widget.MultiAutoCompleteTextView] with hashtag, mention, and hyperlink support.
 *
 * @see SocialView
 */
@Suppress("LeakingThis")
open class SocialAutoCompleteTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.autoCompleteTextViewStyle
) : AppCompatMultiAutoCompleteTextView(context, attrs, defStyleAttr), TextWatcher,
    SocialView<MultiAutoCompleteTextView> by SocialViewImpl() {

    private val enabledSymbols = mutableSetOf<Char>()
    private var isNoThreshold: Boolean = threshold <= 0

    var hashtagAdapter: ArrayAdapter<*>? = null
    var mentionAdapter: ArrayAdapter<*>? = null

    init {
        setup(attrs)
        addTextChangedListener(this)
        if (isHashtagEnabled()) {
            enabledSymbols += '#'
        }
        if (isMentionEnabled()) {
            enabledSymbols += '@'
        }
        setTokenizer(SymbolsTokenizer(enabledSymbols))
    }

    override fun enoughToFilter(): Boolean = when {
        text.isNullOrBlank() -> false
        isNoThreshold -> true
        else -> super.enoughToFilter()
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (isNoThreshold && focused && adapter != null) {
            performFiltering("", 0)
        }
    }

    override fun setThreshold(@IntRange(from = 0) threshold: Int) {
        super.setThreshold(threshold)
        isNoThreshold = threshold <= 0
    }

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

    override fun setHashtagEnabled(enabled: Boolean) {
        super.setHashtagEnabled(enabled)
        enableSymbol('#', enabled)
        setTokenizer(SymbolsTokenizer(enabledSymbols))
    }

    override fun setMentionEnabled(enabled: Boolean) {
        super.setMentionEnabled(enabled)
        enableSymbol('@', enabled)
        setTokenizer(SymbolsTokenizer(enabledSymbols))
    }

    private fun enableSymbol(symbol: Char, enable: Boolean) = when {
        enable -> enabledSymbols += symbol
        else -> enabledSymbols -= symbol
    }

    /**
     * While [MultiAutoCompleteTextView.CommaTokenizer] tracks only comma symbol,
     * [SymbolsTokenizer] can track multiple characters, in this instance,
     * are hashtag and at symbol.
     */
    class SymbolsTokenizer(private val symbols: Set<Char>) : MultiAutoCompleteTextView.Tokenizer {
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
                when {
                    symbols.contains(text[i]) -> return i
                    else -> i++
                }
            }
            return len
        }

        override fun terminateToken(text: CharSequence): CharSequence {
            var i = text.length
            while (i > 0 && text[i - 1] == ' ') {
                i--
            }
            return when {
                i > 0 && symbols.contains(text[i - 1]) -> text
                text is Spanned -> {
                    val sp = SpannableString("$text ")
                    copySpansFrom(text, 0, text.length, Any::class.java, sp, 0)
                    sp
                }
                else -> "$text "
            }
        }
    }
}