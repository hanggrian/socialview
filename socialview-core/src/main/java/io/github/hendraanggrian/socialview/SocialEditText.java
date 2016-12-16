package io.github.hendraanggrian.socialview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.List;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class SocialEditText extends EditText implements SocialViewBase {

    @NonNull private final SocialView socialView;

    public SocialEditText(Context context) {
        super(context);
        socialView = new SocialView(this, context);
    }

    public SocialEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        socialView = new SocialView(this, context, attrs);
    }

    public SocialEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        socialView = new SocialView(this, context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SocialEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        socialView = new SocialView(this, context, attrs);
    }

    @Override
    public void setHashtagColor(@ColorInt int color) {
        socialView.setHashtagColor(color);
    }

    @Override
    public void setHashtagColorRes(@ColorRes int colorRes) {
        socialView.setHashtagColorRes(colorRes);
    }

    @Override
    public void setMentionColor(@ColorInt int color) {
        socialView.setMentionColor(color);
    }

    @Override
    public void setMentionColorRes(@ColorRes int colorRes) {
        socialView.setMentionColorRes(colorRes);
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
    public void setOnHashtagClickListener(@Nullable SocialView.OnSocialClickListener listener) {
        socialView.setOnHashtagClickListener(listener);
    }

    @Override
    public void setOnMentionClickListener(@Nullable SocialView.OnSocialClickListener listener) {
        socialView.setOnMentionClickListener(listener);
    }

    @Override
    public void setOnHashtagEditingListener(@Nullable SocialView.OnSocialEditingListener listener) {
        socialView.setOnHashtagEditingListener(listener);
    }

    @Override
    public void setOnMentionEditingListener(@Nullable SocialView.OnSocialEditingListener listener) {
        socialView.setOnMentionEditingListener(listener);
    }

    @Override
    public int getHashtagColor() {
        return socialView.getHashtagColor();
    }

    @Override
    public int getMentionColor() {
        return socialView.getMentionColor();
    }

    @Override
    public boolean isHashtagEnabled() {
        return socialView.isHashtagEnabled();
    }

    @Override
    public boolean isMentionEnabled() {
        return socialView.isMentionEnabled();
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
}