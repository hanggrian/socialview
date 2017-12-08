package com.hendraanggrian.widget

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import com.hendraanggrian.socialview.SocialView
import com.hendraanggrian.socialview.SocialViewImpl

/**
 * [android.widget.EditText] with hashtag, mention, and hyperlink support.
 *
 * @see SocialView
 * @see SocialViewImpl
 */
class SocialEditText @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = android.support.v7.appcompat.R.attr.editTextStyle,
        impl: SocialViewImpl = SocialViewImpl()
) : AppCompatEditText(context, attrs, defStyleAttr), SocialView by impl {

    init {
        impl.init(this, attrs)
    }
}