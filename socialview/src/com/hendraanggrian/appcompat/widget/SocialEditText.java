package com.hendraanggrian.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.hendraanggrian.appcompat.internal.SocialViewHelper;

import java.util.List;

/**
 * {@link android.widget.EditText} with hashtag, mention, and hyperlink support.
 *
 * @see SocialView
 */
public class SocialEditText extends AppCompatEditText implements SocialView {
    private final SocialView impl;

    public SocialEditText(Context context) {
        this(context, null);
    }

    public SocialEditText(Context context, AttributeSet attrs) {
        this(context, attrs, androidx.appcompat.R.attr.editTextStyle);
    }

    public SocialEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        impl = new SocialViewHelper(this, attrs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isHashtagEnabled() {
        return impl.isHashtagEnabled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMentionEnabled() {
        return impl.isMentionEnabled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isHyperlinkEnabled() {
        return impl.isHyperlinkEnabled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHashtagEnabled(boolean enabled) {
        impl.setHashtagEnabled(enabled);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMentionEnabled(boolean enabled) {
        impl.setMentionEnabled(enabled);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHyperlinkEnabled(boolean enabled) {
        impl.setHyperlinkEnabled(enabled);
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public ColorStateList getHashtagColors() {
        return impl.getHashtagColors();
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public ColorStateList getMentionColors() {
        return impl.getMentionColors();
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public ColorStateList getHyperlinkColors() {
        return impl.getHyperlinkColors();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHashtagColors(@NonNull ColorStateList colors) {
        impl.setHashtagColors(colors);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMentionColors(@NonNull ColorStateList colors) {
        impl.setMentionColors(colors);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHyperlinkColors(@NonNull ColorStateList colors) {
        impl.setHyperlinkColors(colors);
    }

    /**
     * {@inheritDoc}
     */
    @ColorInt
    @Override
    public int getHashtagColor() {
        return impl.getHashtagColor();
    }

    /**
     * {@inheritDoc}
     */
    @ColorInt
    @Override
    public int getMentionColor() {
        return impl.getMentionColor();
    }

    /**
     * {@inheritDoc}
     */
    @ColorInt
    @Override
    public int getHyperlinkColor() {
        return impl.getHyperlinkColor();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHashtagColor(int color) {
        impl.setHashtagColor(color);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMentionColor(int color) {
        impl.setMentionColor(color);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHyperlinkColor(int color) {
        impl.setHyperlinkColor(color);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnHashtagClickListener(@Nullable SocialView.OnClickListener listener) {
        impl.setOnHashtagClickListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnMentionClickListener(@Nullable SocialView.OnClickListener listener) {
        impl.setOnMentionClickListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnHyperlinkClickListener(@Nullable SocialView.OnClickListener listener) {
        impl.setOnHyperlinkClickListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHashtagTextChangedListener(@Nullable OnChangedListener listener) {
        impl.setHashtagTextChangedListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMentionTextChangedListener(@Nullable OnChangedListener listener) {
        impl.setMentionTextChangedListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public List<String> getHashtags() {
        return impl.getHashtags();
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public List<String> getMentions() {
        return impl.getMentions();
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public List<String> getHyperlinks() {
        return impl.getHyperlinks();
    }
}