package com.hendraanggrian.socialview;

import android.support.annotation.NonNull;
import android.widget.TextView;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public interface OnSocialClickListener {

    /**
     * Triggered when a span of hashtag, mention, or hyperlink is clicked.
     *
     * @param v    TextView or its subclasses attached with {@link SocialViewAttacher}.
     * @param type a hashtag, mention, or hyperlink.
     * @param text clicked span, in form of CharSequence.
     */
    void onSocialClick(@NonNull TextView v, @SocialView.Type int type, @NonNull CharSequence text);
}