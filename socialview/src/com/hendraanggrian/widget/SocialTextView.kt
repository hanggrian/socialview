package com.hendraanggrian.widget

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.widget.TextView
import com.hendraanggrian.widget.internal.SocialViewImpl

/**
 * [android.widget.TextView] with hashtag, mention, and hyperlink support.
 *
 * @see SocialView
 */
open class SocialTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr), SocialView<TextView> by SocialViewImpl() {

    init {
        @Suppress("LeakingThis") setup(attrs)
    }
}