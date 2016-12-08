package io.github.hendraanggrian.socialview.commons;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class Hashtag implements Hashtagable {

    @NonNull private final String hashtag;
    @Nullable private final Integer hashtagCount;

    public Hashtag(@NonNull String hashtag) {
        this(hashtag, null);
    }

    public Hashtag(@NonNull String hashtag, @Nullable Integer hashtagCount) {
        this.hashtag = hashtag;
        this.hashtagCount = hashtagCount;
    }

    @NonNull
    @Override
    public String getHashtag() {
        return hashtag;
    }

    @Nullable
    @Override
    public Integer getHashtagCount() {
        return hashtagCount;
    }
}