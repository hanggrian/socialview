package com.hendraanggrian.widget

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.widget.EditText
import com.hendraanggrian.widget.internal.SocialViewImpl

/**
 * [android.widget.EditText] with hashtag, mention, and hyperlink support.
 *
 * @see SocialView
 */
open class SocialEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.support.v7.appcompat.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr), SocialView<EditText> by SocialViewImpl() {

    init {
        @Suppress("LeakingThis") setup(attrs)
    }
}