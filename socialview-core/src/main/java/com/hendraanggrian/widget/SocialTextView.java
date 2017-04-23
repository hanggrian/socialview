package com.hendraanggrian.widget;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.hendraanggrian.socialview.OnSocialClickListener;
import com.hendraanggrian.socialview.SocialView;
import com.hendraanggrian.socialview.SocialTextWatcher;
import com.hendraanggrian.socialview.SocialViewAttacher;

import java.util.List;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class SocialTextView extends AppCompatTextView implements SocialView {

    @NonNull private final SocialViewAttacher attacher;

    public SocialTextView(Context context) {
        this(context, null);
    }

    public SocialTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public SocialTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        attacher = new SocialViewAttacher(this, context, attrs);
    }

    @Override
    public void setHashtagEnabled(boolean enabled) {
        attacher.setHashtagEnabled(enabled);
    }

    @Override
    public void setMentionEnabled(boolean enabled) {
        attacher.setMentionEnabled(enabled);
    }

    @Override
    public void setHyperlinkEnabled(boolean enabled) {
        attacher.setHyperlinkEnabled(enabled);
    }

    @Override
    public void setHashtagColor(@ColorInt int color) {
        attacher.setHashtagColor(color);
    }

    @Override
    public void setHashtagColorRes(@ColorRes int colorRes) {
        attacher.setHashtagColorRes(colorRes);
    }

    @Override
    public void setMentionColor(@ColorInt int color) {
        attacher.setMentionColor(color);
    }

    @Override
    public void setMentionColorRes(@ColorRes int colorRes) {
        attacher.setMentionColorRes(colorRes);
    }

    @Override
    public void setHyperlinkColor(@ColorInt int color) {
        attacher.setHyperlinkColor(color);
    }

    @Override
    public void setHyperlinkColorRes(@ColorRes int colorRes) {
        attacher.setHyperlinkColorRes(colorRes);
    }

    @Override
    public void setOnSocialClickListener(@Nullable OnSocialClickListener listener) {
        attacher.setOnSocialClickListener(listener);
    }

    @Override
    public void setSocialTextChangedListener(@Nullable SocialTextWatcher watcher) {
        attacher.setSocialTextChangedListener(watcher);
    }

    @Override
    public boolean isHashtagEnabled() {
        return attacher.isHashtagEnabled();
    }

    @Override
    public boolean isMentionEnabled() {
        return attacher.isMentionEnabled();
    }

    @Override
    public boolean isHyperlinkEnabled() {
        return attacher.isHyperlinkEnabled();
    }

    @Override
    public int getHashtagColor() {
        return attacher.getHashtagColor();
    }

    @Override
    public int getMentionColor() {
        return attacher.getMentionColor();
    }

    @Override
    public int getHyperlinkColor() {
        return attacher.getHyperlinkColor();
    }

    @NonNull
    @Override
    public List<String> getHashtags() {
        return attacher.getHashtags();
    }

    @NonNull
    @Override
    public List<String> getMentions() {
        return attacher.getMentions();
    }

    @NonNull
    @Override
    public List<String> getHyperlinks() {
        return attacher.getHyperlinks();
    }
}