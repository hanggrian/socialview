package com.hendraanggrian.widget.socialview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public final class SocialViewAttacher implements SociableView, TextWatcher {

    @NonNull private final TextView view;
    @NonNull private final Property hashtag;
    @NonNull private final Property mention;
    @NonNull private final Property hyperlink;

    @Nullable private SocialTextWatcher hashtagWatcher;
    @Nullable private SocialTextWatcher mentionWatcher;

    private boolean isHashtagEditing;
    private boolean isMentionEditing;

    public SocialViewAttacher(@NonNull TextView textView, @NonNull Context context) {
        this(textView, context, null);
    }

    public SocialViewAttacher(@NonNull TextView textView, @NonNull Context context, @Nullable AttributeSet attrs) {
        view = textView;
        view.setText(textView.getText(), TextView.BufferType.SPANNABLE);
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.addTextChangedListener(this);

        Resources.Theme theme = context.getTheme();
        TypedArray array = theme.obtainStyledAttributes(attrs, R.styleable.SocialTextView, 0, 0);
        TypedValue value = new TypedValue();
        try {
            // the only reason why socialview uses appcompat-v7 instead of support-v4 is to get accent color
            int colorAccent = theme.resolveAttribute(R.attr.colorAccent, value, true) ? value.data : view.getCurrentTextColor();
            hashtag = new Property(
                    array.getBoolean(R.styleable.SocialTextView_hashtagEnabled, true),
                    array.getColor(R.styleable.SocialTextView_hashtagColor, colorAccent)
            );
            mention = new Property(
                    array.getBoolean(R.styleable.SocialTextView_mentionEnabled, true),
                    array.getColor(R.styleable.SocialTextView_mentionColor, colorAccent)
            );
            hyperlink = new Property(
                    array.getBoolean(R.styleable.SocialTextView_hyperlinkEnabled, true),
                    array.getColor(R.styleable.SocialTextView_hyperlinkColor, colorAccent)
            );
        } finally {
            array.recycle();
            colorize();
        }
    }

    @Override
    public void setHashtagEnabled(boolean enabled) {
        hashtag.enabled = enabled;
        colorize();
    }

    @Override
    public void setMentionEnabled(boolean enabled) {
        mention.enabled = enabled;
        colorize();
    }

    @Override
    public void setHyperlinkEnabled(boolean enabled) {
        hyperlink.enabled = enabled;
        colorize();
    }

    @Override
    public void setHashtagColor(@ColorInt int color) {
        hashtag.color = color;
        colorize();
    }

    @Override
    public void setHashtagColorRes(@ColorRes int colorRes) {
        setHashtagColor(ContextCompat.getColor(view.getContext(), colorRes));
    }

    @Override
    public void setMentionColor(@ColorInt int color) {
        mention.color = color;
        colorize();
    }

    @Override
    public void setMentionColorRes(@ColorRes int colorRes) {
        setMentionColor(ContextCompat.getColor(view.getContext(), colorRes));
    }

    @Override
    public void setHyperlinkColor(@ColorInt int color) {
        hyperlink.color = color;
        colorize();
    }

    @Override
    public void setHyperlinkColorRes(@ColorRes int colorRes) {
        setHyperlinkColor(ContextCompat.getColor(view.getContext(), colorRes));
    }

    @Override
    public void setOnHashtagClickListener(@Nullable OnSocialClickListener listener) {
        hashtag.listener = listener;
    }

    @Override
    public void setOnMentionClickListener(@Nullable OnSocialClickListener listener) {
        mention.listener = listener;
    }

    @Override
    public void setOnHyperlinkClickListener(@Nullable OnSocialClickListener listener) {
        hyperlink.listener = listener;
    }

    @Override
    public void setHashtagTextChangedListener(@Nullable SocialTextWatcher watcher) {
        hashtagWatcher = watcher;
    }

    @Override
    public void setMentionTextChangedListener(@Nullable SocialTextWatcher watcher) {
        mentionWatcher = watcher;
    }

    @Override
    public boolean isHashtagEnabled() {
        return hashtag.enabled;
    }

    @Override
    public boolean isMentionEnabled() {
        return mention.enabled;
    }

    @Override
    public boolean isHyperlinkEnabled() {
        return hyperlink.enabled;
    }

    @Override
    public int getHashtagColor() {
        return hashtag.color;
    }

    @Override
    public int getMentionColor() {
        return mention.color;
    }

    @Override
    public int getHyperlinkColor() {
        return hyperlink.color;
    }

    @NonNull
    @Override
    public List<String> getHashtags() {
        return extract(compile(REGEX_HASHTAG));
    }

    @NonNull
    @Override
    public List<String> getMentions() {
        return extract(compile(REGEX_MENTION));
    }

    @NonNull
    @Override
    public List<String> getHyperlinks() {
        return extract(compile(REGEX_HYPERLINK));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // triggered when text is backspaced
        Log.d("beforeTextChanged", String.format("s=%s  start=%s    count=%s    after=%s", s, start, count, after));
        if (count > 0 && start > 0) {
            Log.d("charAt", String.valueOf(s.charAt(start - 1)));
            switch (s.charAt(start - 1)) {
                case HASHTAG:
                    isHashtagEditing = true;
                    isMentionEditing = false;
                    break;
                case MENTION:
                    isHashtagEditing = false;
                    isMentionEditing = true;
                    break;
                default:
                    if (!Character.isLetterOrDigit(s.charAt(start - 1))) {
                        isHashtagEditing = false;
                        isMentionEditing = false;
                    } else if (hashtagWatcher != null && isHashtagEditing) {
                        hashtagWatcher.onTextChanged(view, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start - 1) + 1, start).toString());
                    } else if (mentionWatcher != null && isMentionEditing) {
                        mentionWatcher.onTextChanged(view, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start - 1) + 1, start).toString());
                    }
                    break;
            }
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d("onTextChanged", String.format("s=%s  start=%s    before=%s   count=%s", s, start, before, count));
        if (s.length() > 0) {
            final Spannable spannable = (Spannable) view.getText();
            for (CharacterStyle style : spannable.getSpans(0, s.length(), CharacterStyle.class))
                spannable.removeSpan(style);
            colorize(spannable);

            // triggered when text is added
            if (start < s.length()) {
                Log.d("charAt", String.valueOf(s.charAt(start + count - 1)));
                switch (s.charAt(start + count - 1)) {
                    case HASHTAG:
                        isHashtagEditing = true;
                        isMentionEditing = false;
                        break;
                    case MENTION:
                        isHashtagEditing = false;
                        isMentionEditing = true;
                        break;
                    default:
                        if (!Character.isLetterOrDigit(s.charAt(start))) {
                            isHashtagEditing = false;
                            isMentionEditing = false;
                        } else if (hashtagWatcher != null && isHashtagEditing) {
                            hashtagWatcher.onTextChanged(view, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start) + 1, start + count).toString());
                        } else if (mentionWatcher != null && isMentionEditing) {
                            mentionWatcher.onTextChanged(view, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start) + 1, start + count).toString());
                        }
                        break;
                }
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    private void colorize() {
        colorize(view.getText());
    }

    private void colorize(CharSequence text) {
        if (hashtag.enabled) {
            Matcher hashtagMatcher = Pattern.compile(REGEX_HASHTAG).matcher(text);
            while (hashtagMatcher.find())
                ((Spannable) text).setSpan(hashtag.createSpan(), hashtagMatcher.start(), hashtagMatcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if (mention.enabled) {
            Matcher mentionMatcher = Pattern.compile(REGEX_MENTION).matcher(text);
            while (mentionMatcher.find())
                ((Spannable) text).setSpan(mention.createSpan(), mentionMatcher.start(), mentionMatcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if (hyperlink.enabled) {
            Matcher hyperlinkMatcher = Pattern.compile(REGEX_HYPERLINK).matcher(text);
            while (hyperlinkMatcher.find())
                ((Spannable) text).setSpan(hyperlink.createSpan(), hyperlinkMatcher.start(), hyperlinkMatcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private int indexOfNextNonLetterDigit(CharSequence text, int start) {
        for (int i = start + 1; i < text.length(); i++)
            if (!Character.isLetterOrDigit(text.charAt(i)))
                return i;
        return text.length();
    }

    private int indexOfPreviousNonLetterDigit(CharSequence text, int start, int end) {
        for (int i = end; i > start; i--)
            if (!Character.isLetterOrDigit(text.charAt(i)))
                return i;
        return start;
    }

    @NonNull
    private List<String> extract(Pattern pattern) {
        List<String> list = new ArrayList<>();
        Matcher matcher = pattern.matcher(view.getText().toString());
        while (matcher.find())
            list.add(matcher.group(1));
        return list;
    }

    @NonNull
    public static SocialViewAttacher attach(@NonNull TextView view) {
        return new SocialViewAttacher(view, view.getContext());
    }
}