package com.hendraanggrian.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
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
import com.hendraanggrian.socialview.SociableView;
import com.hendraanggrian.socialview.SocialTextWatcher;
import com.hendraanggrian.socialview.SociableViewImpl;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class SocialAutoCompleteTextView<H, M> extends AppCompatMultiAutoCompleteTextView implements SociableView, TextWatcher {

    @NonNull private final SociableView impl;
    private ArrayAdapter<H> hashtagAdapter;
    private ArrayAdapter<M> mentionAdapter;

    public SocialAutoCompleteTextView(Context context) {
        this(context, null);
    }

    public SocialAutoCompleteTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.autoCompleteTextViewStyle);
    }

    public SocialAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        impl = SociableViewImpl.attach(this, context, attrs);
        setTokenizer(new HashtagMentionTokenizer(isHashtagEnabled(), isMentionEnabled()));
        setThreshold(1);
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
        setTokenizer(new HashtagMentionTokenizer(isHashtagEnabled(), isMentionEnabled()));
    }

    @Override
    public void setMentionEnabled(boolean enabled) {
        impl.setMentionEnabled(enabled);
        setTokenizer(new HashtagMentionTokenizer(isHashtagEnabled(), isMentionEnabled()));
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

    private static class HashtagMentionTokenizer implements Tokenizer {
        private final Collection<Character> symbols = new ArrayList<>();

        private HashtagMentionTokenizer(boolean hashtagEnabled, boolean mentionEnabled) {
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