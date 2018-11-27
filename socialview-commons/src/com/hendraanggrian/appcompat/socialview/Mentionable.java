package com.hendraanggrian.appcompat.socialview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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