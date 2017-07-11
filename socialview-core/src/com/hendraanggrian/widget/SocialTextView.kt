package com.hendraanggrian.widget

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import com.hendraanggrian.socialview.SociableView
import com.hendraanggrian.socialview.SociableViewImpl

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class SocialTextView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = android.R.attr.textViewStyle) : AppCompatEditText(context, attrs, defStyleAttr), SociableView {

    private val impl = SociableViewImpl(this, attrs)

    override val internalContext: Context get() = context
    override val hashtags: Collection<String> get() = impl.hashtags
    override val mentions: Collection<String> get() = impl.mentions
    override val hyperlinks: Collection<String> get() = impl.hyperlinks

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

    override fun setOnHashtagClickListener(listener: ((SociableView, CharSequence) -> Unit)?) = impl.setOnHashtagClickListener(listener)
    override fun setOnMentionClickListener(listener: ((SociableView, CharSequence) -> Unit)?) = impl.setOnMentionClickListener(listener)

    override fun setHashtagTextChangedListener(watcher: ((SociableView, CharSequence) -> Unit)?) = impl.setHashtagTextChangedListener(watcher)
    override fun setMentionTextChangedListener(watcher: ((SociableView, CharSequence) -> Unit)?) = impl.setMentionTextChangedListener(watcher)
}