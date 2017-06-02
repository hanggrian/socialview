package com.hendraanggrian.socialview;

import android.support.annotation.NonNull;
import android.widget.TextView;

/**
 * @author Hendra Anggrian (com.hendraanggrian@gmail.com)
 */
public interface OnSocialClickListener {

    /**
     * Triggered when a span of hashtag, mention, or hyperlink is clicked.
     *
     * @param v    TextView or its subclasses attached with {@link SociableViewImpl}.
     * @param text clicked span, in form of CharSequence.
     */
    void onSocialClick(@NonNull TextView v, @NonNull String text);
}