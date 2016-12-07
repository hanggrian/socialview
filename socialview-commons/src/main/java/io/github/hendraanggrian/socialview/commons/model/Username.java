package io.github.hendraanggrian.socialview.commons.model;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class Username {

    @NonNull private final String username;
    @NonNull private final Object avatar;

    public Username(@NonNull String username) {
        this.username = username;
        this.avatar = new Object();
    }

    public Username(@NonNull String username, @NonNull String avatarUrl) {
        this.username = username;
        this.avatar = avatarUrl;
    }

    public Username(@NonNull String username, @NonNull Drawable avatarDrawable) {
        this.username = username;
        this.avatar = avatarDrawable;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    @NonNull
    public Object getAvatar() {
        return avatar;
    }
}