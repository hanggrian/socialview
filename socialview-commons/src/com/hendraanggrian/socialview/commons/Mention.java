package com.hendraanggrian.socialview.commons;

import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;

/**
 * A data class of mention to be used with {@link MentionAdapter}.
 *
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class Mention {

    @NonNull private final String username;
    @Nullable private String displayname;
    @Nullable private Object avatar;

    public Mention(@NonNull String username) {
        this.username = username;
    }

    public Mention(@NonNull String username, @Nullable String displayname) {
        this.username = username;
        this.displayname = displayname;
    }

    private Mention(@NonNull String username, @Nullable String displayname, @Nullable Object avatar) {
        this.username = username;
        this.displayname = displayname;
        this.avatar = avatar;
    }

    public Mention(@NonNull String username, @Nullable String displayname, @DrawableRes int avatarDrawable) {
        this(username, displayname, (Object) avatarDrawable);
    }

    public Mention(@NonNull String username, @Nullable String displayname, @Nullable String avatarURL) {
        this(username, displayname, (Object) avatarURL);
    }

    public Mention(@NonNull String username, @Nullable String displayname, @Nullable Uri avatarUri) {
        this(username, displayname, (Object) avatarUri);
    }

    public Mention(@NonNull String username, @Nullable String displayname, @Nullable File avatarFile) {
        this(username, displayname, (Object) avatarFile);
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

    private void setAvatar(@Nullable Object avatar) {
        this.avatar = avatar;
    }

    public void setAvatarDrawable(@DrawableRes int avatarRes) {
        this.avatar = avatarRes;
    }

    public void setAvatarURL(@Nullable String avatarUrl) {
        this.avatar = avatarUrl;
    }

    public void setAvatarUri(@Nullable Uri avatarUri) {
        this.avatar = avatarUri;
    }

    public void setAvatarUri(@Nullable File avatarFile) {
        this.avatar = avatarFile;
    }
}