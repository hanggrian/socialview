package com.hendraanggrian.appcompat.widget

import android.content.Context
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils.copySpansFrom
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
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
) : AppCompatMultiAutoCompleteTextView(context, attrs, defStyleAttr),
    SocialView<MultiAutoCompleteTextView> by SocialViewImpl() {

    // TODO: should check for symbols closest to cursor, not s[start]
    private val textWatcher = object : TextWatcher {
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

    private var isHashtagEnabled: Boolean
    private var isMentionEnabled: Boolean

    init {
        setup(attrs)
        addTextChangedListener(textWatcher)
        isHashtagEnabled = isHashtagEnabled()
        isMentionEnabled = isMentionEnabled()
        setTokenizer(CharTokenizer())
    }

    override fun setHashtagEnabled(enabled: Boolean) {
        super.setHashtagEnabled(enabled)
        isHashtagEnabled = enabled
        setTokenizer(CharTokenizer())
    }

    override fun setMentionEnabled(enabled: Boolean) {
        super.setMentionEnabled(enabled)
        isMentionEnabled = enabled
        setTokenizer(CharTokenizer())
    }

    /**
     * While [MultiAutoCompleteTextView.CommaTokenizer] tracks only comma symbol,
     * [CharTokenizer] can track multiple characters, in this instance,
     * are hashtag and at symbol.
     */
    private inner class CharTokenizer : Tokenizer {

        private var chars: CharArray

        init {
            val list = mutableListOf<Char>()
            if (isHashtagEnabled) {
                list += '#'
            }
            if (isMentionEnabled) {
                list += '@'
            }
            chars = list.toCharArray()
        }

        override fun findTokenStart(text: CharSequence, cursor: Int): Int {
            var i = cursor
            while (i > 0 && text[i - 1] !in chars) {
                i--
            }
            while (i < cursor && text[i] == ' ') {
                i++
            }
            // imperfect fix for dropdown still showing without symbol found
            if (i == 0 && isPopupShowing) {
                dismissDropDown()
            }
            return i
        }

        override fun findTokenEnd(text: CharSequence, cursor: Int): Int {
            var i = cursor
            val len = text.length
            while (i < len) {
                when {
                    text[i] in chars -> return i
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
            return if (i > 0 && text[i - 1] in chars) {
                text
            } else {
                if (text is Spanned) {
                    val sp = SpannableString("$text ")
                    copySpansFrom(text, 0, text.length, Any::class.java, sp, 0)
                    sp
                } else {
                    "$text "
                }
            }
        }
    }
}