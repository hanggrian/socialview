package io.github.hendraanggrian.socialview;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public interface Mentionable {

    @NonNull
    String getUsername();

    @Nullable
    String getDisplayname();

    @Nullable
    Object getAvatar();
}