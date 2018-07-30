package com.hendraanggrian.appcompat.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import com.hendraanggrian.appcompat.internal.SocialViewImpl

/**
 * [android.widget.EditText] with hashtag, mention, and hyperlink support.
 *
 * @see SocialView
 */
open class SocialEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr), SocialView<EditText> by SocialViewImpl() {

    init {
        @Suppress("LeakingThis") setup(attrs)
    }
}