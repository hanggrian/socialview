package com.hendraanggrian.socialview;

import android.support.annotation.NonNull;

/**
 * TODO: improve TextWatcher logic. https://github.com/HendraAnggrian/socialview/issues/7
 *
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public interface SocialTextWatcher {

    void onSocialTextChanged(@NonNull SociableView v, @NonNull CharSequence s);
}