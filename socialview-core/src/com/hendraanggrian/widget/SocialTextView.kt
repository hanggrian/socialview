package com.hendraanggrian.widget

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import com.hendraanggrian.socialview.SocialView
import com.hendraanggrian.socialview.SocialViewImpl

/**
 * [android.widget.TextView] with hashtag, mention, and hyperlink support.
 *
 * @see SocialView
 * @see SocialViewImpl
 */
class SocialTextView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = android.R.attr.textViewStyle,
        impl: SocialViewImpl = SocialViewImpl()
) : AppCompatTextView(context, attrs, defStyleAttr), SocialView by impl {

    init {
        impl.init(this, attrs)
    }
}