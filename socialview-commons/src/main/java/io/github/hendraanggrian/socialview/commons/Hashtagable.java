package io.github.hendraanggrian.socialview.commons;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public interface Hashtagable {

    @NonNull
    String getHashtag();

    @Nullable
    Integer getHashtagCount();
}