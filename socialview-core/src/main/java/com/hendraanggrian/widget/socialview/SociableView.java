package com.hendraanggrian.widget.socialview;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import java.util.List;

/**
 * @author Hendra Anggian (hendraanggrian@gmail.com)
 */
public interface SociableView {

    char HASHTAG = '#';
    char MENTION = '@';
    String REGEX_HASHTAG = "#(\\w+)";
    String REGEX_MENTION = "@(\\w+)";
    String REGEX_HYPERLINK = "[a-z]+:\\/\\/[^ \\n]*";

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

    interface OnSocialClickListener {
        void onClick(@NonNull TextView view, @NonNull CharSequence text);
    }

    interface SocialTextWatcher {
        void onTextChanged(@NonNull TextView view, @NonNull CharSequence text);
    }

    class Property {
        boolean enabled;
        @ColorInt int color;
        @Nullable OnSocialClickListener listener;

        Property(boolean enabled, @ColorInt int color) {
            this.enabled = enabled;
            this.color = color;
        }

        SocialSpan createSpan() {
            return new SocialSpan(color, listener);
        }
    }
}