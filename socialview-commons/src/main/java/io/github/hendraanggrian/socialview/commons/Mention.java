package io.github.hendraanggrian.socialview.commons;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

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

    public void setAvatar(@NonNull Context context, @DrawableRes int avatarRes) {
        this.avatar = ContextCompat.getDrawable(context, avatarRes);
    }

    public void setAvatar(@Nullable Drawable avatar) {
        this.avatar = avatar;
    }

    public void setAvatar(@Nullable String avatarUrl) {
        this.avatar = avatarUrl;
    }
}