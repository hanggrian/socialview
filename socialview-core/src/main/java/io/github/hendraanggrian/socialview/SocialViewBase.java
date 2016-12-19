package io.github.hendraanggrian.socialview;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * @author Hendra Anggian (hendraanggrian@gmail.com)
 */
public interface SocialViewBase {

    void setHashtagColor(@ColorInt int color);

    void setHashtagColorRes(@ColorRes int colorRes);

    void setMentionColor(@ColorInt int color);

    void setMentionColorRes(@ColorRes int colorRes);

    void setHashtagEnabled(boolean enabled);

    void setMentionEnabled(boolean enabled);

    void setOnHashtagClickListener(@Nullable SocialViewAttacher.OnSocialClickListener listener);

    void setOnMentionClickListener(@Nullable SocialViewAttacher.OnSocialClickListener listener);

    void setOnHashtagEditingListener(@Nullable SocialViewAttacher.OnSocialEditingListener listener);

    void setOnMentionEditingListener(@Nullable SocialViewAttacher.OnSocialEditingListener listener);

    @ColorInt
    int getHashtagColor();

    @ColorInt
    int getMentionColor();

    boolean isHashtagEnabled();

    boolean isMentionEnabled();

    @NonNull
    List<String> getHashtags();

    @NonNull
    List<String> getMentions();
}