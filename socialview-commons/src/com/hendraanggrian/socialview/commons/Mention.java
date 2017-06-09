package com.hendraanggrian.socialview.commons;

import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;

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

    private void setAvatar(@Nullable Object avatar) {
        this.avatar = avatar;
    }

    @SuppressWarnings("UnnecessaryBoxing")
    public void setAvatarDrawable(@DrawableRes int avatarRes) {
        this.avatar = Integer.valueOf(avatarRes);
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

    public static class Builder {
        @NonNull private final String username;
        @Nullable private String displayname;
        @Nullable private Object avatar;

        public Builder(@NonNull String username) {
            this.username = username;
        }

        @NonNull
        public Builder setDisplayname(@Nullable String displayname) {
            this.displayname = displayname;
            return this;
        }

        @NonNull
        @SuppressWarnings("UnnecessaryBoxing")
        public Builder setAvatarDrawable(@DrawableRes int avatarRes) {
            this.avatar = Integer.valueOf(avatarRes);
            return this;
        }

        @NonNull
        public Builder setAvatarURL(@Nullable String avatarUrl) {
            this.avatar = avatarUrl;
            return this;
        }

        @NonNull
        public Builder setAvatarUri(@Nullable Uri avatarUri) {
            this.avatar = avatarUri;
            return this;
        }

        @NonNull
        public Builder setAvatarUri(@Nullable File avatarFile) {
            this.avatar = avatarFile;
            return this;
        }

        @NonNull
        public Mention build() {
            Mention mention = new Mention(username);
            mention.setDisplayname(displayname);
            mention.setAvatar(avatar);
            return mention;
        }
    }
}