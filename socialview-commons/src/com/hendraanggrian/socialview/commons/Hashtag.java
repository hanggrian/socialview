package com.hendraanggrian.socialview.commons;

import android.support.annotation.NonNull;

/**
 * @author Hendra Anggrian (com.hendraanggrian@gmail.com)
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
        @NonNull private final String hashtag;
        private int count;

        public Builder(@NonNull String hashtag) {
            this.hashtag = hashtag;
        }

        @NonNull
        public Builder setCount(int count) {
            this.count = count;
            return this;
        }

        @NonNull
        public Hashtag build() {
            Hashtag hashtag = new Hashtag(this.hashtag);
            hashtag.setCount(count);
            return hashtag;
        }
    }
}