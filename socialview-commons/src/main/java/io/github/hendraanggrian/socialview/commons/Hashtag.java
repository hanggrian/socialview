package io.github.hendraanggrian.socialview.commons;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class Hashtag {

    @NonNull private final String hashtag;
    @Nullable private Integer count;

    public Hashtag(@NonNull String hashtag) {
        this.hashtag = hashtag;
    }

    @NonNull
    public String getHashtag() {
        return hashtag;
    }

    @Nullable
    public Integer getCount() {
        return count;
    }

    public void setCount(@Nullable Integer count) {
        this.count = count;
    }
}