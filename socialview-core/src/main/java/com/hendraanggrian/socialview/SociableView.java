package com.hendraanggrian.socialview;

import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collection;

/**
 * Base methods of all socialview's widgets.
 * The logic, however, are calculated in {@link SociableViewImpl} while the widgets are just
 * passing these methods to the attacher.
 *
 * @author Hendra Anggian (hendraanggrian@gmail.com)
 * @see SociableViewImpl
 */
public interface SociableView {

    boolean isHashtagEnabled();

    boolean isMentionEnabled();

    boolean isHyperlinkEnabled();

    void setHashtagEnabled(boolean enabled);

    void setMentionEnabled(boolean enabled);

    void setHyperlinkEnabled(boolean enabled);

    @ColorInt
    int getHashtagColor();

    @ColorInt
    int getMentionColor();

    @ColorInt
    int getHyperlinkColor();

    void setHashtagColor(@ColorInt int color);

    void setMentionColor(@ColorInt int color);

    void setHyperlinkColor(@ColorInt int color);

    void setHashtagColorRes(@ColorRes int colorRes);

    void setMentionColorRes(@ColorRes int colorRes);

    void setHyperlinkColorRes(@ColorRes int colorRes);

    void setHashtagColorAttr(@AttrRes int colorAttr);

    void setMentionColorAttr(@AttrRes int colorAttr);

    void setHyperlinkColorAttr(@AttrRes int colorAttr);

    void setOnHashtagClickListener(@Nullable OnSocialClickListener listener);

    void setOnMentionClickListener(@Nullable OnSocialClickListener listener);

    void setHashtagTextChangedListener(@Nullable SocialTextWatcher watcher);

    void setMentionTextChangedListener(@Nullable SocialTextWatcher watcher);

    @NonNull
    Collection<String> getHashtags();

    @NonNull
    Collection<String> getMentions();

    @NonNull
    Collection<String> getHyperlinks();
}