package com.hendraanggrian.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.hendraanggrian.socialview.OnSocialClickListener;
import com.hendraanggrian.socialview.SocialTextWatcher;
import com.hendraanggrian.socialview.SocialView;
import com.hendraanggrian.socialview.SocialViewAttacher;

import java.util.List;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class SocialTextView extends AppCompatTextView implements SocialView {

    @NonNull private final SocialView socialView;

    public SocialTextView(Context context) {
        this(context, null);
    }

    public SocialTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public SocialTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        socialView = SocialViewAttacher.attach(this, context, attrs);
    }

    @Override
    public void setHashtagEnabled(boolean enabled) {
        socialView.setHashtagEnabled(enabled);
    }

    @Override
    public void setMentionEnabled(boolean enabled) {
        socialView.setMentionEnabled(enabled);
    }

    @Override
    public void setHyperlinkEnabled(boolean enabled) {
        socialView.setHyperlinkEnabled(enabled);
    }

    @Override
    public void setHashtagUnderlined(boolean underlined) {
        socialView.setHashtagUnderlined(underlined);
    }

    @Override
    public void setMentionUnderlined(boolean underlined) {
        socialView.setMentionUnderlined(underlined);
    }

    @Override
    public void setHyperlinkUnderlined(boolean underlined) {
        socialView.setHyperlinkUnderlined(underlined);
    }

    @Override
    public void setHashtagColor(@ColorInt int color) {
        socialView.setHashtagColor(color);
    }

    @Override
    public void setMentionColor(@ColorInt int color) {
        socialView.setMentionColor(color);
    }

    @Override
    public void setHyperlinkColor(@ColorInt int color) {
        socialView.setHyperlinkColor(color);
    }

    @Override
    public void setHashtagColorRes(@ColorRes int colorRes) {
        socialView.setHashtagColorRes(colorRes);
    }

    @Override
    public void setMentionColorRes(@ColorRes int colorRes) {
        socialView.setMentionColorRes(colorRes);
    }

    @Override
    public void setHyperlinkColorRes(@ColorRes int colorRes) {
        socialView.setHyperlinkColorRes(colorRes);
    }

    @Override
    public void setHashtagColorAttr(@AttrRes int colorAttr) {
        socialView.setHashtagColorAttr(colorAttr);
    }

    @Override
    public void setMentionColorAttr(@AttrRes int colorAttr) {
        socialView.setMentionColorAttr(colorAttr);
    }

    @Override
    public void setHyperlinkColorAttr(@AttrRes int colorAttr) {
        socialView.setHyperlinkColorAttr(colorAttr);
    }

    @Override
    public void setOnSocialClickListener(@Nullable OnSocialClickListener listener) {
        socialView.setOnSocialClickListener(listener);
    }

    @Override
    public void setSocialTextChangedListener(@Nullable SocialTextWatcher watcher) {
        socialView.setSocialTextChangedListener(watcher);
    }

    @Override
    public boolean isHashtagEnabled() {
        return socialView.isHashtagEnabled();
    }

    @Override
    public boolean isMentionEnabled() {
        return socialView.isMentionEnabled();
    }

    @Override
    public boolean isHyperlinkEnabled() {
        return socialView.isHyperlinkEnabled();
    }

    @Override
    public boolean isHashtagUnderlined() {
        return false;
    }

    @Override
    public boolean isMentionUnderlined() {
        return false;
    }

    @Override
    public boolean isHyperlinkUnderlined() {
        return false;
    }

    @ColorInt
    @Override
    public int getHashtagColor() {
        return socialView.getHashtagColor();
    }

    @ColorInt
    @Override
    public int getMentionColor() {
        return socialView.getMentionColor();
    }

    @ColorInt
    @Override
    public int getHyperlinkColor() {
        return socialView.getHyperlinkColor();
    }

    @NonNull
    @Override
    public List<String> getHashtags() {
        return socialView.getHashtags();
    }

    @NonNull
    @Override
    public List<String> getMentions() {
        return socialView.getMentions();
    }

    @NonNull
    @Override
    public List<String> getHyperlinks() {
        return socialView.getHyperlinks();
    }
}