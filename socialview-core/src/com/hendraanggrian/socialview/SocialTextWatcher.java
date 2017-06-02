package com.hendraanggrian.socialview;

import android.support.annotation.NonNull;
import android.widget.TextView;

/**
 * TODO: improve TextWatcher logic. https://github.com/HendraAnggrian/socialview/issues/7
 *
 * @author Hendra Anggrian (com.hendraanggrian@gmail.com)
 */
public interface SocialTextWatcher {

    void onSocialTextChanged(@NonNull TextView v, @NonNull CharSequence s);
}