package com.hendraanggrian.socialview.commons;

import android.support.annotation.NonNull;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class Hashtag {

    @NonNull private final String hashtag;
    private int count;

    public Hashtag(@NonNull String hashtag) {
        this.hashtag = hashtag;
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

    public static class Builder {
        private final Hashtag hashtag;

        public Builder(@NonNull String hashtag) {
            this.hashtag = new Hashtag(hashtag);
        }

        @NonNull
        public Builder setCount(int count) {
            hashtag.setCount(count);
            return this;
        }

        @NonNull
        public Hashtag build() {
            return hashtag;
        }
    }
}