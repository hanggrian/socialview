@file:JvmName("SocialEditText")

package com.hendraanggrian.widget

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import com.hendraanggrian.socialview.SocialView
import com.hendraanggrian.socialview.SocialViewHelper

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class SocialEditText @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = android.support.v7.appcompat.R.attr.editTextStyle) :
        AppCompatEditText(context, attrs, defStyleAttr), SocialView {

    private val helper = SocialViewHelper.attach(this, attrs)
    override val view = this

    override var isHashtagEnabled
        get() = helper.isHashtagEnabled
        set(value) {
            helper.isHashtagEnabled = value
        }
    override var isMentionEnabled
        get() = helper.isMentionEnabled
        set(value) {
            helper.isMentionEnabled = value
        }
    override var isHyperlinkEnabled
        get() = helper.isHyperlinkEnabled
        set(value) {
            helper.isHyperlinkEnabled = value
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

    override fun setOnHashtagClickListener(listener: ((SocialView, CharSequence) -> Unit)?) = helper.setOnHashtagClickListener(listener)
    override fun setOnMentionClickListener(listener: ((SocialView, CharSequence) -> Unit)?) = helper.setOnMentionClickListener(listener)
    override fun setHashtagTextChangedListener(watcher: ((SocialView, CharSequence) -> Unit)?) = helper.setHashtagTextChangedListener(watcher)
    override fun setMentionTextChangedListener(watcher: ((SocialView, CharSequence) -> Unit)?) = helper.setMentionTextChangedListener(watcher)

    override fun colorize() = helper.colorize()
    override fun detach() = helper.detach()
}