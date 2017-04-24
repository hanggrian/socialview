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

import com.hendraanggrian.socialview.OnSocialClickListener;
import com.hendraanggrian.socialview.SocialTextWatcher;
import com.hendraanggrian.socialview.SocialView;
import com.hendraanggrian.socialview.SocialViewAttacher;
import com.hendraanggrian.socialview.commons.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class SocialAutoCompleteTextView<H, M> extends AppCompatMultiAutoCompleteTextView implements SocialView, TextWatcher {

    @NonNull private final SocialView socialView;
    private ArrayAdapter<H> hashtagAdapter;
    private ArrayAdapter<M> mentionAdapter;

    public SocialAutoCompleteTextView(Context context) {
        this(context, null);
    }

    public SocialAutoCompleteTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.autoCompleteTextViewStyle);
    }

    public SocialAutoCompleteTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        socialView = SocialViewAttacher.attach(this, context, attrs);
        setTokenizer(new SocialTokenizer(isHashtagEnabled(), isMentionEnabled()));
        setThreshold(1);
    }

    @Override
    public void setHashtagEnabled(boolean enabled) {
        socialView.setHashtagEnabled(enabled);
        setTokenizer(new SocialTokenizer(isHashtagEnabled(), isMentionEnabled()));
    }

    @Override
    public void setMentionEnabled(boolean enabled) {
        socialView.setMentionEnabled(enabled);
        setTokenizer(new SocialTokenizer(isHashtagEnabled(), isMentionEnabled()));
    }

    @Override
    public void setHyperlinkEnabled(boolean enabled) {
        socialView.setHyperlinkEnabled(enabled);
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
    public void setHyperlinkColor(@ColorInt int color) {
        socialView.setHyperlinkColor(color);
    }

    @Override
    public void setHyperlinkColorRes(@ColorRes int colorRes) {
        socialView.setHyperlinkColorRes(colorRes);
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
    public int getHashtagColor() {
        return socialView.getHashtagColor();
    }

    @Override
    public int getMentionColor() {
        return socialView.getMentionColor();
    }

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