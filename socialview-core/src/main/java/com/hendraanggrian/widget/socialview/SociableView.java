package com.hendraanggrian.widget.socialview;

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
public interface SociableView {

    //region setters
    void setHashtagEnabled(boolean enabled);

    void setMentionEnabled(boolean enabled);

    void setHyperlinkEnabled(boolean enabled);

    void setHashtagColor(@ColorInt int color);

    void setHashtagColorRes(@ColorRes int colorRes);

    void setMentionColor(@ColorInt int color);

    void setMentionColorRes(@ColorRes int colorRes);

    void setHyperlinkColor(@ColorInt int color);

    void setHyperlinkColorRes(@ColorRes int colorRes);

    void setOnHashtagClickListener(@Nullable OnSocialClickListener listener);

    void setOnMentionClickListener(@Nullable OnSocialClickListener listener);

    void setOnHyperlinkClickListener(@Nullable OnSocialClickListener listener);

    void setHashtagTextChangedListener(@Nullable SocialTextWatcher watcher);

    void setMentionTextChangedListener(@Nullable SocialTextWatcher watcher);
    //endregion

    //region getters
    boolean isHashtagEnabled();

    boolean isMentionEnabled();

    boolean isHyperlinkEnabled();

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
    //endregion
}