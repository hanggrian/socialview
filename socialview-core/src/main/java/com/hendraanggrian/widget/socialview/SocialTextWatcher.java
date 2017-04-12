package com.hendraanggrian.widget.socialview;

import android.support.annotation.NonNull;
import android.widget.TextView;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public interface SocialTextWatcher {

    void onTextChanged(@NonNull TextView view, @NonNull CharSequence text);
}