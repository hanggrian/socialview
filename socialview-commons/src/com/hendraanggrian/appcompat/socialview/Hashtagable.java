package com.hendraanggrian.appcompat.socialview;

import androidx.annotation.NonNull;

public interface Hashtagable {

    /**
     * Unique id of this hashtag.
     */
    @NonNull
    CharSequence getHashtag();

    /**
     * Optional count, located right to hashtag name.
     */
    int getCount();
}