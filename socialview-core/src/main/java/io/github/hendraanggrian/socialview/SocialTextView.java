package io.github.hendraanggrian.socialview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.List;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class SocialTextView extends TextView implements SocialViewBase {

    private final SocialViewAttacher attacher;

    public SocialTextView(Context context) {
        super(context);
        attacher = new SocialViewAttacher(this, context);
    }

    public SocialTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        attacher = new SocialViewAttacher(this, context, attrs);
    }

    public SocialTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        attacher = new SocialViewAttacher(this, context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SocialTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        attacher = new SocialViewAttacher(this, context, attrs);
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
    public void setHashtagEnabled(boolean enabled) {
        attacher.setHashtagEnabled(enabled);
    }

    @Override
    public void setMentionEnabled(boolean enabled) {
        attacher.setMentionEnabled(enabled);
    }

    @Override
    public void setOnHashtagClickListener(@Nullable SocialViewAttacher.OnSocialClickListener listener) {
        attacher.setOnHashtagClickListener(listener);
    }

    @Override
    public void setOnMentionClickListener(@Nullable SocialViewAttacher.OnSocialClickListener listener) {
        attacher.setOnMentionClickListener(listener);
    }

    @Override
    public void setOnHashtagEditingListener(@Nullable SocialViewAttacher.OnSocialEditingListener listener) {
        attacher.setOnHashtagEditingListener(listener);
    }

    @Override
    public void setOnMentionEditingListener(@Nullable SocialViewAttacher.OnSocialEditingListener listener) {
        attacher.setOnMentionEditingListener(listener);
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
    public boolean isHashtagEnabled() {
        return attacher.isHashtagEnabled();
    }

    @Override
    public boolean isMentionEnabled() {
        return attacher.isMentionEnabled();
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
}