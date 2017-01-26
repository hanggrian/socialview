package io.github.hendraanggrian.socialview.commons;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class Mention {

    @NonNull private final String username;
    @Nullable private String displayname;
    @Nullable private Object avatar;

    public Mention(@NonNull String username) {
        this.username = username;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    @Nullable
    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(@Nullable String displayname) {
        this.displayname = displayname;
    }

    @Nullable
    public Object getAvatar() {
        return avatar;
    }

    public void setAvatar(@DrawableRes int avatarRes) {
        this.avatar = avatarRes;
    }

    public void setAvatar(@Nullable String avatarUrl) {
        this.avatar = avatarUrl;
    }

    private void setAvatar(@Nullable Object avatar) {
        this.avatar = avatar;
    }
}