package io.github.hendraanggrian.socialview.commons;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class LabelAvatar extends Label {

    @NonNull final Object avatar;

    LabelAvatar(@NonNull String label, @NonNull String url) {
        super(label);
        this.avatar = url;
    }

    LabelAvatar(@NonNull String label, @NonNull Drawable drawable) {
        super(label);
        this.avatar = drawable;
    }
}