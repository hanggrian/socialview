package com.hanggrian.appcompat.socialview.autocomplete;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Abstract mention to be used with
 * {@link com.hanggrian.appcompat.socialview.widget.MentionArrayAdapter}.
 */
public interface Mentionable {
    /**
     * Unique id of this mention.
     */
    @NonNull
    CharSequence getUsername();

    /**
     * Optional display name, located above username.
     */
    @Nullable
    CharSequence getDisplayname();

    /**
     * Optional avatar, may be Drawable, resources, or string url pointing to image.
     */
    @Nullable
    Object getAvatar();
}
