package com.hendraanggrian.widget

import android.content.Context
import android.content.res.ColorStateList
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import com.hendraanggrian.socialview.SocialView
import com.hendraanggrian.socialview.SocialViewImpl

class SocialTextView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr), SocialView {

    private val mImpl: SocialView = SocialViewImpl(this, attrs)

    override var isHashtagEnabled: Boolean
        get() = mImpl.isHashtagEnabled
        set(enabled) {
            mImpl.isHashtagEnabled = enabled
        }

    override var isMentionEnabled: Boolean
        get() = mImpl.isMentionEnabled
        set(enabled) {
            mImpl.isMentionEnabled = enabled
        }

    override var isHyperlinkEnabled: Boolean
        get() = mImpl.isHyperlinkEnabled
        set(enabled) {
            mImpl.isHyperlinkEnabled = enabled
        }

    override var hashtagColor: ColorStateList
        get() = mImpl.hashtagColor
        set(color) {
            mImpl.hashtagColor = color
        }

    override var mentionColor: ColorStateList
        get() = mImpl.mentionColor
        set(color) {
            mImpl.mentionColor = color
        }

    override var hyperlinkColor: ColorStateList
        get() = mImpl.hyperlinkColor
        set(color) {
            mImpl.hyperlinkColor = color
        }

    override fun setOnHashtagClickListener(listener: ((view: SocialView, String) -> Unit)?) = mImpl.setOnHashtagClickListener(listener)

    override fun setOnMentionClickListener(listener: ((view: SocialView, String) -> Unit)?) = mImpl.setOnMentionClickListener(listener)

    override fun setOnHyperlinkClickListener(listener: ((view: SocialView, String) -> Unit)?) = mImpl.setOnHyperlinkClickListener(listener)

    override fun setHashtagTextChangedListener(watcher: ((view: SocialView, String) -> Unit)?) = mImpl.setHashtagTextChangedListener(watcher)

    override fun setMentionTextChangedListener(watcher: ((view: SocialView, String) -> Unit)?) = mImpl.setMentionTextChangedListener(watcher)

    override fun colorize() = mImpl.colorize()
}