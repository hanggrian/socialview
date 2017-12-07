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

    override var isHashtagEnabled: Boolean = mImpl.isHashtagEnabled

    override var isMentionEnabled: Boolean = mImpl.isMentionEnabled

    override var isHyperlinkEnabled: Boolean = mImpl.isHyperlinkEnabled

    override var hashtagColor: ColorStateList = mImpl.hashtagColor

    override var mentionColor: ColorStateList = mImpl.mentionColor

    override var hyperlinkColor: ColorStateList = mImpl.hyperlinkColor

    override fun setOnHashtagClickListener(listener: ((view: SocialView, String) -> Unit)?) = mImpl.setOnHashtagClickListener(listener)

    override fun setOnMentionClickListener(listener: ((view: SocialView, String) -> Unit)?) = mImpl.setOnMentionClickListener(listener)

    override fun setOnHyperlinkClickListener(listener: ((view: SocialView, String) -> Unit)?) = mImpl.setOnHyperlinkClickListener(listener)

    override fun setHashtagTextChangedListener(watcher: ((view: SocialView, String) -> Unit)?) = mImpl.setHashtagTextChangedListener(watcher)

    override fun setMentionTextChangedListener(watcher: ((view: SocialView, String) -> Unit)?) = mImpl.setMentionTextChangedListener(watcher)

    override fun colorize() = mImpl.colorize()
}