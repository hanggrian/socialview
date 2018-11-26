package com.hendraanggrian.appcompat.widget;

import android.content.res.ColorStateList;

import java.util.List;
import java.util.regex.Pattern;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.PatternsCompat;

public interface SocialView {

    int FLAG_HASHTAG = 1;
    int FLAG_MENTION = 2;
    int FLAG_HYPERLINK = 4;

    Pattern PATTERN_HASHTAG = Pattern.compile("#(\\w+)");
    Pattern PATTERN_MENTION = Pattern.compile("@(\\w+)");
    Pattern PATTERN_HYPERLINK = PatternsCompat.WEB_URL;

    /**
     * Determine whether this view should find and span hashtags.
     */
    boolean isHashtagEnabled();

    /**
     * Enable or disable hashtag spanning, if not already.
     */
    void setHashtagEnabled(boolean enabled);

    /**
     * Determine whether this view should find and span mentions.
     */
    boolean isMentionEnabled();

    /**
     * Enable or disable mention spanning, if not already.
     */
    void setMentionEnabled(boolean enabled);

    /**
     * Determine whether this view should find and span hyperlinks.
     */
    boolean isHyperlinkEnabled();

    /**
     * Enable or disable hyperlink spanning, if not already.
     */
    void setHyperlinkEnabled(boolean enabled);

    /**
     * Color of hashtag spans. Default is color accent of current app theme.
     */
    ColorStateList getHashtagColors();

    void setHashtagColors(@NonNull ColorStateList colors);

    /**
     * Color of mention spans. Default is color accent of current app theme.
     */
    ColorStateList getMentionColors();

    void setMentionColors(@NonNull ColorStateList colors);

    /**
     * Color of hyperlink spans. Default is color accent of current app theme.
     */
    ColorStateList getHyperlinkColors();

    void setHyperlinkColors(@NonNull ColorStateList colors);

    /**
     * Get and set hashtag color from color integer.
     */
    int getHashtagColor();

    void setHashtagColor(@ColorInt int color);

    /**
     * Get and set mention color from color integer.
     */
    int getMentionColor();

    void setMentionColor(@ColorInt int color);

    /**
     * Get and set hyperlink color from color integer.
     */
    int getHyperlinkColor();

    void setHyperlinkColor(@ColorInt int color);

    /**
     * Register a callback to be invoked when hashtag is clicked.
     */
    void setOnHashtagClickListener(@Nullable OnClickListener listener);

    /**
     * Register a callback to be invoked when mention is clicked.
     */
    void setOnMentionClickListener(@Nullable OnClickListener listener);

    /**
     * Register a callback to be invoked when hyperlink is clicked.
     */
    void setOnHyperlinkClickListener(@Nullable OnClickListener listener);

    /**
     * Register a text watcher to be invoked when hashtag is modified.
     */
    void setHashtagTextChangedListener(@Nullable OnChangedListener listener);

    /**
     * Register a text watcher to be invoked when mention is modified.
     */
    void setMentionTextChangedListener(@Nullable OnChangedListener listener);

    /**
     * Obtain all hashtags in current text.
     */
    List<String> getHashtags();

    /**
     * Obtain all mentions in current text.
     */
    List<String> getMentions();

    /**
     * Obtain all hyperlinks in current text.
     */
    List<String> getHyperlinks();

    interface OnClickListener {

        void onClick(@NonNull SocialView view, @NonNull CharSequence text);
    }

    interface OnChangedListener {

        void onChanged(@NonNull SocialView view, @NonNull CharSequence text);
    }
}