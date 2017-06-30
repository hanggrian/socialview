package com.hendraanggrian.socialview.commons;

import android.support.annotation.NonNull;

/**
 * A data class of hashtag to be used with {@link HashtagAdapter}.
 *
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class Hashtag {

    @NonNull private final String hashtag;
    private int count;

    public Hashtag(@NonNull String hashtag) {
        this.hashtag = hashtag;
    }

    public Hashtag(@NonNull String hashtag, int count) {
        this.hashtag = hashtag;
        this.count = count;
    }

    @NonNull
    public String getHashtag() {
        return hashtag;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}