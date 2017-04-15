package com.hendraanggrian.widget.socialview.commons;

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
        this(username, null);
    }

    public Mention(@NonNull String username, @Nullable String displayname) {
        this(username, displayname, null);
    }

    public Mention(@NonNull String username, @Nullable String displayname, @Nullable String avatarUrl) {
        this(username, displayname, (Object) avatarUrl);
    }

    public Mention(@NonNull String username, @Nullable String displayname, @DrawableRes int drawableRes) {
        this(username, displayname, (Object) drawableRes);
    }

    private Mention(@NonNull String username, @Nullable String displayname, @Nullable Object avatar) {
        this.username = username;
        this.displayname = displayname;
        this.avatar = avatar;
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
}