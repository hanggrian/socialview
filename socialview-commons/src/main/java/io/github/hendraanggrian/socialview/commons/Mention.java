package io.github.hendraanggrian.socialview.commons;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class Mention implements Mentionable {

    @NonNull private String username;
    @Nullable private String displayname;
    @Nullable private Object avatar;

    public Mention(@NonNull String username) {
        this(username, null, (Object) null);
    }

    public Mention(@NonNull String username, @Nullable String displayname) {
        this(username, displayname, (Object) null);
    }

    public Mention(@NonNull String username, @Nullable String displayname, @DrawableRes int avatarDrawable) {
        this(username, displayname, (Object) avatarDrawable);
    }

    public Mention(@NonNull String username, @Nullable String displayname, @Nullable String avatarUrl) {
        this(username, displayname, (Object) avatarUrl);
    }

    private Mention(@NonNull String username, @Nullable String displayname, @Nullable Object avatar) {
        this.username = username;
        this.displayname = displayname;
        this.avatar = avatar;
    }

    @NonNull
    @Override
    public String getUsername() {
        return username;
    }

    @Nullable
    @Override
    public String getDisplayname() {
        return displayname;
    }

    @Nullable
    @Override
    public Object getAvatar() {
        return avatar;
    }
}