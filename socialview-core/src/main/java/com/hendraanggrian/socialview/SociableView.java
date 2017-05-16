package com.hendraanggrian.socialview;

import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * Base methods of all socialview's widgets.
 * The logic, however, are calculated in {@link SocialViewImpl} while the widgets are just
 * passing these methods to the attacher.
 *
 * @author Hendra Anggian (hendraanggrian@gmail.com)
 * @see SocialViewImpl
 */
public interface SociableView {

    int TYPE_HASHTAG = 1;
    int TYPE_MENTION = 2;
    int TYPE_HYPERLINK = 4;

    boolean isHashtagEnabled();

    boolean isMentionEnabled();

    boolean isHyperlinkEnabled();

    void setHashtagEnabled(boolean enabled);

    void setMentionEnabled(boolean enabled);

    void setHyperlinkEnabled(boolean enabled);

    boolean isHashtagUnderlined();

    boolean isMentionUnderlined();

    boolean isHyperlinkUnderlined();

    void setHashtagUnderlined(boolean underlined);

    void setMentionUnderlined(boolean underlined);

    void setHyperlinkUnderlined(boolean underlined);

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

    void setOnSocialClickListener(@Nullable OnSocialClickListener listener);

    void setSocialTextChangedListener(@Nullable SocialTextWatcher watcher);

    @NonNull
    List<String> getHashtags();

    @NonNull
    List<String> getMentions();

    @NonNull
    List<String> getHyperlinks();

    @IntDef({TYPE_HASHTAG, TYPE_MENTION, TYPE_HYPERLINK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }
}