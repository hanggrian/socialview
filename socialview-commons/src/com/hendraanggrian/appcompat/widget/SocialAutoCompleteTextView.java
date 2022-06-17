package com.hendraanggrian.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;

import com.hendraanggrian.appcompat.internal.SocialViewHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * {@link android.widget.MultiAutoCompleteTextView} with hashtag, mention, and hyperlink support.
 *
 * @see SocialView
 */
public class SocialAutoCompleteTextView extends AppCompatMultiAutoCompleteTextView implements SocialView {
    private final SocialView helper;

    // TODO: should check for symbols closest to cursor, not s[start]
    @SuppressWarnings("FieldCanBeLocal")
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(s) && start < s.length()) {
                switch (s.charAt(start)) {
                    case '#':
                        if (getAdapter() != hashtagAdapter) {
                            setAdapter(hashtagAdapter);
                        }
                        break;
                    case '@':
                        if (getAdapter() != mentionAdapter) {
                            setAdapter(mentionAdapter);
                        }
                        break;
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private ArrayAdapter<?> hashtagAdapter;
    private ArrayAdapter<?> mentionAdapter;

    public SocialAutoCompleteTextView(Context context) {
        this(context, null);
    }

    public SocialAutoCompleteTextView(Context context, AttributeSet attrs) {
        this(context, attrs, androidx.appcompat.R.attr.autoCompleteTextViewStyle);
    }

    public SocialAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        helper = SocialViewHelper.install(this, attrs);
        addTextChangedListener(textWatcher);
        setTokenizer(new CharTokenizer());
    }

    @Nullable
    public ArrayAdapter<?> getHashtagAdapter() {
        return hashtagAdapter;
    }

    public void setHashtagAdapter(@Nullable ArrayAdapter<?> adapter) {
        hashtagAdapter = adapter;
    }

    @Nullable
    public ArrayAdapter<?> getMentionAdapter() {
        return mentionAdapter;
    }

    public void setMentionAdapter(@Nullable ArrayAdapter<?> adapter) {
        mentionAdapter = adapter;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Pattern getHashtagPattern() {
        return helper.getHashtagPattern();
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Pattern getMentionPattern() {
        return helper.getMentionPattern();
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Pattern getHyperlinkPattern() {
        return helper.getHyperlinkPattern();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHashtagPattern(@Nullable Pattern pattern) {
        helper.setHashtagPattern(pattern);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMentionPattern(@Nullable Pattern pattern) {
        helper.setMentionPattern(pattern);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHyperlinkPattern(@Nullable Pattern pattern) {
        helper.setHyperlinkPattern(pattern);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isHashtagEnabled() {
        return helper.isHashtagEnabled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMentionEnabled() {
        return helper.isMentionEnabled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isHyperlinkEnabled() {
        return helper.isHyperlinkEnabled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHashtagEnabled(boolean enabled) {
        helper.setHashtagEnabled(enabled);
        setTokenizer(new CharTokenizer());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMentionEnabled(boolean enabled) {
        helper.setMentionEnabled(enabled);
        setTokenizer(new CharTokenizer());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHyperlinkEnabled(boolean enabled) {
        helper.setHyperlinkEnabled(enabled);
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public ColorStateList getHashtagColors() {
        return helper.getHashtagColors();
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public ColorStateList getMentionColors() {
        return helper.getMentionColors();
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public ColorStateList getHyperlinkColors() {
        return helper.getHyperlinkColors();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHashtagColors(@NonNull ColorStateList colors) {
        helper.setHashtagColors(colors);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMentionColors(@NonNull ColorStateList colors) {
        helper.setMentionColors(colors);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHyperlinkColors(@NonNull ColorStateList colors) {
        helper.setHyperlinkColors(colors);
    }

    /**
     * {@inheritDoc}
     */
    @ColorInt
    @Override
    public int getHashtagColor() {
        return helper.getHashtagColor();
    }

    /**
     * {@inheritDoc}
     */
    @ColorInt
    @Override
    public int getMentionColor() {
        return helper.getMentionColor();
    }

    /**
     * {@inheritDoc}
     */
    @ColorInt
    @Override
    public int getHyperlinkColor() {
        return helper.getHyperlinkColor();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHashtagColor(@ColorInt int color) {
        helper.setHashtagColor(color);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMentionColor(@ColorInt int color) {
        helper.setMentionColor(color);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHyperlinkColor(@ColorInt int color) {
        helper.setHyperlinkColor(color);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnHashtagClickListener(@Nullable SocialView.OnClickListener listener) {
        helper.setOnHashtagClickListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnMentionClickListener(@Nullable SocialView.OnClickListener listener) {
        helper.setOnMentionClickListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnHyperlinkClickListener(@Nullable SocialView.OnClickListener listener) {
        helper.setOnHyperlinkClickListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHashtagTextChangedListener(@Nullable OnChangedListener listener) {
        helper.setHashtagTextChangedListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMentionTextChangedListener(@Nullable OnChangedListener listener) {
        helper.setMentionTextChangedListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public List<String> getHashtags() {
        return helper.getHashtags();
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public List<String> getMentions() {
        return helper.getMentions();
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public List<String> getHyperlinks() {
        return helper.getHyperlinks();
    }

    /**
     * While `CommaTokenizer` tracks only comma symbol,
     * `CharTokenizer` can track multiple characters, in this instance, are hashtag and at symbol.
     *
     * @see android.widget.MultiAutoCompleteTextView.CommaTokenizer
     */
    private class CharTokenizer implements Tokenizer {
        private final Collection<Character> chars = new ArrayList<>();

        CharTokenizer() {
            if (isHashtagEnabled()) {
                chars.add('#');
            }
            if (isMentionEnabled()) {
                chars.add('@');
            }
        }

        @Override
        public int findTokenStart(CharSequence text, int cursor) {
            int i = cursor;

            while (i > 0 && !chars.contains(text.charAt(i - 1))) {
                i--;
            }
            while (i < cursor && text.charAt(i) == ' ') {
                i++;
            }

            // imperfect fix for dropdown still showing without symbol found
            if (i == 0 && isPopupShowing()) {
                dismissDropDown();
            }
            return i;
        }

        @Override
        public int findTokenEnd(CharSequence text, int cursor) {
            int i = cursor;
            int len = text.length();

            while (i < len) {
                if (chars.contains(text.charAt(i))) {
                    return i;
                } else {
                    i++;
                }
            }

            return len;
        }

        @Override
        public CharSequence terminateToken(CharSequence text) {
            int i = text.length();

            while (i > 0 && text.charAt(i - 1) == ' ') {
                i--;
            }

            if (i > 0 && chars.contains(text.charAt(i - 1))) {
                return text;
            } else {
                if (text instanceof Spanned) {
                    final Spannable sp = new SpannableString(text + " ");
                    TextUtils.copySpansFrom((Spanned) text, 0, text.length(), Object.class, sp, 0);
                    return sp;
                } else {
                    return text + " ";
                }
            }
        }
    }
}
