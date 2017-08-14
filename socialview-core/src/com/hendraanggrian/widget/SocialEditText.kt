package com.hendraanggrian.widget

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import com.hendraanggrian.socialview.SocialView
import com.hendraanggrian.socialview.SocialViewImpl

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class SocialEditText @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = android.support.v7.appcompat.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr), SocialView {

    private var mImpl = SocialViewImpl(this, attrs)

    override var isHashtagEnabled
        get() = mImpl.isHashtagEnabled
        set(value) {
            mImpl.isHashtagEnabled = value
        }
    override var isMentionEnabled
        get() = mImpl.isMentionEnabled
        set(value) {
            mImpl.isMentionEnabled = value
        }
    override var isHyperlinkEnabled
        get() = mImpl.isHyperlinkEnabled
        set(value) {
            mImpl.isHyperlinkEnabled = value
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

    override fun setOnHashtagClickListener(listener: ((SocialView, CharSequence) -> Unit)?): Unit = mImpl.setOnHashtagClickListener(listener)
    override fun setOnMentionClickListener(listener: ((SocialView, CharSequence) -> Unit)?): Unit = mImpl.setOnMentionClickListener(listener)
    override fun setOnHyperlinkClickListener(listener: ((SocialView, CharSequence) -> Unit)?): Unit = mImpl.setOnHyperlinkClickListener(listener)
    override fun setHashtagTextChangedListener(watcher: ((SocialView, CharSequence) -> Unit)?): Unit = mImpl.setHashtagTextChangedListener(watcher)
    override fun setMentionTextChangedListener(watcher: ((SocialView, CharSequence) -> Unit)?): Unit = mImpl.setMentionTextChangedListener(watcher)

    override fun colorize(): Unit = mImpl.colorize()
}