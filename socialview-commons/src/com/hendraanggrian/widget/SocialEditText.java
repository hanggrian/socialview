package com.hendraanggrian.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.hendraanggrian.socialview.OnSocialClickListener;
import com.hendraanggrian.socialview.SociableView;
import com.hendraanggrian.socialview.SocialTextWatcher;
import com.hendraanggrian.socialview.SociableViewImpl;

import java.util.Collection;

/**
 * @author Hendra Anggrian (com.hendraanggrian@gmail.com)
 */
public class SocialEditText extends AppCompatEditText implements SociableView {

    @NonNull private final SociableView impl;

    public SocialEditText(Context context) {
        this(context, null);
    }

    public SocialEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.editTextStyle);
    }

    public SocialEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        impl = SociableViewImpl.attach(this, context, attrs);
    }

    @Override
    public boolean isHashtagEnabled() {
        return impl.isHashtagEnabled();
    }

    @Override
    public boolean isMentionEnabled() {
        return impl.isMentionEnabled();
    }

    @Override
    public boolean isHyperlinkEnabled() {
        return impl.isHyperlinkEnabled();
    }

    @Override
    public void setHashtagEnabled(boolean enabled) {
        impl.setHashtagEnabled(enabled);
    }

    @Override
    public void setMentionEnabled(boolean enabled) {
        impl.setMentionEnabled(enabled);
    }

    @Override
    public void setHyperlinkEnabled(boolean enabled) {
        impl.setHyperlinkEnabled(enabled);
    }

    @ColorInt
    @Override
    public int getHashtagColor() {
        return impl.getHashtagColor();
    }

    @ColorInt
    @Override
    public int getMentionColor() {
        return impl.getMentionColor();
    }

    @ColorInt
    @Override
    public int getHyperlinkColor() {
        return impl.getHyperlinkColor();
    }

    @Override
    public void setHashtagColor(@ColorInt int color) {
        impl.setHashtagColor(color);
    }

    @Override
    public void setMentionColor(@ColorInt int color) {
        impl.setMentionColor(color);
    }

    @Override
    public void setHyperlinkColor(@ColorInt int color) {
        impl.setHyperlinkColor(color);
    }

    @Override
    public void setHashtagColorRes(@ColorRes int colorRes) {
        impl.setHashtagColorRes(colorRes);
    }

    @Override
    public void setMentionColorRes(@ColorRes int colorRes) {
        impl.setMentionColorRes(colorRes);
    }

    @Override
    public void setHyperlinkColorRes(@ColorRes int colorRes) {
        impl.setHyperlinkColorRes(colorRes);
    }

    @Override
    public void setHashtagColorAttr(@AttrRes int colorAttr) {
        impl.setHashtagColorAttr(colorAttr);
    }

    @Override
    public void setMentionColorAttr(@AttrRes int colorAttr) {
        impl.setMentionColorAttr(colorAttr);
    }

    @Override
    public void setHyperlinkColorAttr(@AttrRes int colorAttr) {
        impl.setHyperlinkColorAttr(colorAttr);
    }

    @Override
    public void setOnHashtagClickListener(@Nullable OnSocialClickListener listener) {
        impl.setOnHashtagClickListener(listener);
    }

    @Override
    public void setOnMentionClickListener(@Nullable OnSocialClickListener listener) {
        impl.setOnMentionClickListener(listener);
    }

    @Override
    public void setHashtagTextChangedListener(@Nullable SocialTextWatcher watcher) {
        impl.setHashtagTextChangedListener(watcher);
    }

    @Override
    public void setMentionTextChangedListener(@Nullable SocialTextWatcher watcher) {
        impl.setMentionTextChangedListener(watcher);
    }

    @NonNull
    @Override
    public Collection<String> getHashtags() {
        return impl.getHashtags();
    }

    @NonNull
    @Override
    public Collection<String> getMentions() {
        return impl.getMentions();
    }

    @NonNull
    @Override
    public Collection<String> getHyperlinks() {
        return impl.getHyperlinks();
    }
}