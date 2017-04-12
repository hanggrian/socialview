package com.hendraanggrian.widget;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

import com.hendraanggrian.widget.socialview.OnSocialClickListener;
import com.hendraanggrian.widget.socialview.SociableView;
import com.hendraanggrian.widget.socialview.SocialTextWatcher;
import com.hendraanggrian.widget.socialview.SocialViewAttacher;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class SocialAutoCompleteTextView<H, M> extends AppCompatMultiAutoCompleteTextView implements SociableView, TextWatcher {

    @NonNull private final SocialViewAttacher attacher;
    private ArrayAdapter<H> hashtagAdapter;
    private ArrayAdapter<M> mentionAdapter;

    public SocialAutoCompleteTextView(Context context) {
        super(context);
        attacher = new SocialViewAttacher(this, context);
        init();
    }

    public SocialAutoCompleteTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        attacher = new SocialViewAttacher(this, context, attrs);
        init();
    }

    public SocialAutoCompleteTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        attacher = new SocialViewAttacher(this, context, attrs);
        init();
    }

    @Override
    public void setHashtagEnabled(boolean enabled) {
        attacher.setHashtagEnabled(enabled);
        init();
    }

    @Override
    public void setMentionEnabled(boolean enabled) {
        attacher.setMentionEnabled(enabled);
        init();
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
    public void setOnHashtagClickListener(@Nullable OnSocialClickListener listener) {
        attacher.setOnHashtagClickListener(listener);
    }

    @Override
    public void setOnMentionClickListener(@Nullable OnSocialClickListener listener) {
        attacher.setOnMentionClickListener(listener);
    }

    @Override
    public void setOnHyperlinkClickListener(@Nullable OnSocialClickListener listener) {
        attacher.setOnHyperlinkClickListener(listener);
    }

    @Override
    public void setHashtagTextChangedListener(@Nullable SocialTextWatcher watcher) {
        attacher.setHashtagTextChangedListener(watcher);
    }

    @Override
    public void setMentionTextChangedListener(@Nullable SocialTextWatcher watcher) {
        attacher.setMentionTextChangedListener(watcher);
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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0)
            if (start < s.length())
                switch (s.charAt(start)) {
                    case '#':
                        if (getAdapter() != hashtagAdapter)
                            setAdapter(hashtagAdapter);
                        break;
                    case '@':
                        if (getAdapter() != mentionAdapter)
                            setAdapter(mentionAdapter);
                        break;
                }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    public void setHashtagAdapter(@NonNull ArrayAdapter<H> adapter) {
        hashtagAdapter = adapter;
    }

    public ArrayAdapter<H> getHashtagAdapter() {
        return hashtagAdapter;
    }

    public void setMentionAdapter(@NonNull ArrayAdapter<M> adapter) {
        mentionAdapter = adapter;
    }

    public ArrayAdapter<M> getMentionAdapter() {
        return mentionAdapter;
    }

    private void init() {
        setTokenizer(new SocialTokenizer(isHashtagEnabled(), isMentionEnabled()));
        setThreshold(1);
    }

    private static class SocialTokenizer implements Tokenizer {
        private final List<Character> symbols = new ArrayList<>();

        private SocialTokenizer(boolean hashtagEnabled, boolean mentionEnabled) {
            if (hashtagEnabled)
                symbols.add('#');
            if (mentionEnabled)
                symbols.add('@');
        }

        public int findTokenStart(CharSequence text, int cursor) {
            int i = cursor;
            while (i > 0 && !symbols.contains(text.charAt(i - 1)))
                i--;
            while (i < cursor && text.charAt(i) == ' ')
                i++;
            return i;
        }

        public int findTokenEnd(CharSequence text, int cursor) {
            int i = cursor;
            int len = text.length();
            while (i < len)
                if (symbols.contains(text.charAt(i)))
                    return i;
                else
                    i++;
            return len;
        }

        public CharSequence terminateToken(CharSequence text) {
            int i = text.length();
            while (i > 0 && text.charAt(i - 1) == ' ')
                i--;

            if (i > 0 && symbols.contains(text.charAt(i - 1))) {
                return text;
            } else if (text instanceof Spanned) {
                final SpannableString sp = new SpannableString(text + " ");
                TextUtils.copySpansFrom((Spanned) text, 0, text.length(), Object.class, sp, 0);
                return sp;
            } else {
                return text + " ";
            }
        }
    }
}