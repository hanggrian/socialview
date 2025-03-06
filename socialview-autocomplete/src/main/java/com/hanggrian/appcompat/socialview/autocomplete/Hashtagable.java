package com.hanggrian.appcompat.socialview.autocomplete;

import androidx.annotation.NonNull;

/**
 * Abstract hashtag to be used with
 * {@link com.hanggrian.appcompat.socialview.widget.HashtagArrayAdapter}.
 */
public interface Hashtagable {
    /**
     * Unique id of this hashtag.
     */
    @NonNull
    CharSequence getId();

    /**
     * Optional count, located right to hashtag name.
     */
    int getCount();
}
