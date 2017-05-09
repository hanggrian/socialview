package com.hendraanggrian.socialview;

import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Base methods of all socialview's widgets.
 * The logic, however, are calculated in {@link SocialViewAttacher} while the widgets are just
 * passing these methods to the attacher.
 *
 * @author Hendra Anggian (hendraanggrian@gmail.com)
 * @see SocialViewAttacher
 * @see com.hendraanggrian.widget.SocialTextView
 * @see com.hendraanggrian.widget.SocialEditText
 */
public interface SocialView {

    void setHashtagEnabled(boolean enabled);

    void setMentionEnabled(boolean enabled);

    void setHyperlinkEnabled(boolean enabled);

    void setHashtagUnderlined(boolean underlined);

    void setMentionUnderlined(boolean underlined);

    void setHyperlinkUnderlined(boolean underlined);

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

    boolean isHashtagEnabled();

    boolean isMentionEnabled();

    boolean isHyperlinkEnabled();

    boolean isHashtagUnderlined();

    boolean isMentionUnderlined();

    boolean isHyperlinkUnderlined();

    @ColorInt
    int getHashtagColor();

    @ColorInt
    int getMentionColor();

    @ColorInt
    int getHyperlinkColor();

    @NonNull
    List<String> getHashtags();

    @NonNull
    List<String> getMentions();

    @NonNull
    List<String> getHyperlinks();

    enum Type {
        HASHTAG,
        MENTION,
        HYPERLINK
    }
}