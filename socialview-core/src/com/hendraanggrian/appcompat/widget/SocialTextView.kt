package com.hendraanggrian.appcompat.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.hendraanggrian.appcompat.internal.SocialViewImpl

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