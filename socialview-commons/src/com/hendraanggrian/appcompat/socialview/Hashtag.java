package com.hendraanggrian.appcompat.socialview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A data class of hashtag to be used with {@link com.hendraanggrian.widget.HashtagAdapter}.
 */
public class Hashtag implements Hashtagable {

    private final CharSequence hashtag;
    private final int count;

    public Hashtag(@NonNull CharSequence hashtag) {
        this(hashtag, -1);
    }

    public Hashtag(@NonNull CharSequence hashtag, int count) {
        this.hashtag = hashtag;
        this.count = count;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof Hashtag && ((Hashtag) obj).hashtag == hashtag;
    }

    @Override
    public int hashCode() {
        return hashtag.hashCode();
    }

    @NonNull
    @Override
    public String toString() {
        return hashtag.toString();
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public CharSequence getHashtag() {
        return hashtag;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {
        return count;
    }
}