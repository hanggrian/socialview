package com.hendraanggrian.widget

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.widget.TextView
import com.hendraanggrian.socialview.SocialView
import com.hendraanggrian.socialview.SocialViewImpl

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class SocialEditText @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = android.support.v7.appcompat.R.attr.editTextStyle) : AppCompatEditText(context, attrs, defStyleAttr), SocialView {

    private val impl = SocialViewImpl(this, attrs)

    override val view: TextView = this

    override var isHashtagEnabled: Boolean
        get() = impl.isHashtagEnabled
        set(value) {
            impl.isHashtagEnabled = value
        }
    override var isMentionEnabled: Boolean
        get() = impl.isMentionEnabled
        set(value) {
            impl.isMentionEnabled = value
        }
    override var isHyperlinkEnabled: Boolean
        get() = impl.isHyperlinkEnabled
        set(value) {
            impl.isHyperlinkEnabled = value
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

    override fun getOnHashtagClickListener(): ((SocialView, CharSequence) -> Unit)? = impl.getOnHashtagClickListener()
    override fun getOnMentionClickListener(): ((SocialView, CharSequence) -> Unit)? = impl.getOnMentionClickListener()

    override fun setOnHashtagClickListener(listener: ((SocialView, CharSequence) -> Unit)?) = impl.setOnHashtagClickListener(listener)
    override fun setOnMentionClickListener(listener: ((SocialView, CharSequence) -> Unit)?) = impl.setOnMentionClickListener(listener)

    override fun setHashtagTextChangedListener(watcher: ((SocialView, CharSequence) -> Unit)?) = impl.setHashtagTextChangedListener(watcher)
    override fun setMentionTextChangedListener(watcher: ((SocialView, CharSequence) -> Unit)?) = impl.setMentionTextChangedListener(watcher)
}