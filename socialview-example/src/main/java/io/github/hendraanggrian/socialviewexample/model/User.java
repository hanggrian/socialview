package io.github.hendraanggrian.socialviewexample.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.github.hendraanggrian.socialview.Mentionable;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class User implements Mentionable {

    private String username;
    private String displayname;
    private String url;

    public User(String username, String displayname, String url) {
        this.username = username;
        this.displayname = displayname;
        this.url = url;
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
        return url;
    }
}