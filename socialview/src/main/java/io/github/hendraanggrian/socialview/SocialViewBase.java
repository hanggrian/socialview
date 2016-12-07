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

    void setUsernameColor(@ColorInt int color);

    void setUsernameColorRes(@ColorRes int colorRes);

    void setHashtagEnabled(boolean enabled);

    void setUsernameEnabled(boolean enabled);

    void setOnSocialClickListener(@Nullable SocialView.OnSocialClickListener listener);

    @ColorInt
    int getHashtagColor();

    @ColorInt
    int getUsernameColor();

    boolean isHashtagEnabled();

    boolean isUsernameEnabled();

    @NonNull
    List<String> getHashtags();

    @NonNull
    List<String> getHashtags(boolean withSymbol);

    @NonNull
    List<String> getUsernames();

    @NonNull
    List<String> getUsernames(boolean withSymbol);
}