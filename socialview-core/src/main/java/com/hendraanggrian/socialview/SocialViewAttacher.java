package com.hendraanggrian.socialview;

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
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public final class SocialViewAttacher implements SocialView, TextWatcher {

    private static final String REGEX_HASHTAG = "#(\\w+)";
    private static final String REGEX_MENTION = "@(\\w+)";
    private static final String REGEX_HYPERLINK = "[a-z]+:\\/\\/[^ \\n]*";

    @Nullable private static WeakReference<Pattern> pattern_hashtag;
    @Nullable private static WeakReference<Pattern> pattern_mention;
    @Nullable private static WeakReference<Pattern> pattern_hyperlink;
    private static boolean debug;

    @NonNull private final TextView view;
    private boolean hashtagEnabled;
    private boolean mentionEnabled;
    private boolean hyperlinkEnabled;
    private int hashtagColor;
    private int mentionColor;
    private int hyperlinkColor;
    @Nullable private OnSocialClickListener listener;
    @Nullable private SocialTextWatcher watcher;

    private boolean isHashtagEditing;
    private boolean isMentionEditing;

    private SocialViewAttacher(@NonNull TextView textView, @NonNull Context context, @Nullable AttributeSet attrs) {
        view = textView;
        view.setText(textView.getText(), TextView.BufferType.SPANNABLE);
        view.addTextChangedListener(this);
        Resources.Theme theme = context.getTheme();
        int defaultColor = !view.isInEditMode()
                ? ThemeUtils.getColor(theme, view.getCurrentTextColor())
                : view.getCurrentTextColor();
        TypedArray array = theme.obtainStyledAttributes(attrs, R.styleable.SocialTextView, 0, 0);
        try {
            hashtagEnabled = array.getBoolean(R.styleable.SocialTextView_hashtagEnabled, true);
            mentionEnabled = array.getBoolean(R.styleable.SocialTextView_mentionEnabled, true);
            hyperlinkEnabled = array.getBoolean(R.styleable.SocialTextView_hyperlinkEnabled, true);
            hashtagColor = array.getColor(R.styleable.SocialTextView_hashtagColor, defaultColor);
            mentionColor = array.getColor(R.styleable.SocialTextView_mentionColor, defaultColor);
            hyperlinkColor = array.getColor(R.styleable.SocialTextView_hyperlinkColor, defaultColor);
        } finally {
            array.recycle();
            colorize();
        }
    }

    @Override
    public void setHashtagEnabled(boolean enabled) {
        hashtagEnabled = enabled;
        colorize();
    }

    @Override
    public void setMentionEnabled(boolean enabled) {
        mentionEnabled = enabled;
        colorize();
    }

    @Override
    public void setHyperlinkEnabled(boolean enabled) {
        hyperlinkEnabled = enabled;
        colorize();
    }

    @Override
    public void setHashtagColor(@ColorInt int color) {
        hashtagColor = color;
        colorize();
    }

    @Override
    public void setHashtagColorRes(@ColorRes int colorRes) {
        setHashtagColor(ContextCompat.getColor(view.getContext(), colorRes));
    }

    @Override
    public void setMentionColor(@ColorInt int color) {
        mentionColor = color;
        colorize();
    }

    @Override
    public void setMentionColorRes(@ColorRes int colorRes) {
        setMentionColor(ContextCompat.getColor(view.getContext(), colorRes));
    }

    @Override
    public void setHyperlinkColor(@ColorInt int color) {
        hyperlinkColor = color;
        colorize();
    }

    @Override
    public void setHyperlinkColorRes(@ColorRes int colorRes) {
        setHyperlinkColor(ContextCompat.getColor(view.getContext(), colorRes));
    }

    @Override
    public void setOnSocialClickListener(@Nullable OnSocialClickListener listener) {
        this.listener = listener;
        this.view.setMovementMethod(LinkMovementMethod.getInstance());
        colorize();
    }

    @Override
    public void setSocialTextChangedListener(@Nullable SocialTextWatcher watcher) {
        this.watcher = watcher;
    }

    @Override
    public boolean isHashtagEnabled() {
        return hashtagEnabled;
    }

    @Override
    public boolean isMentionEnabled() {
        return mentionEnabled;
    }

    @Override
    public boolean isHyperlinkEnabled() {
        return hyperlinkEnabled;
    }

    @Override
    public int getHashtagColor() {
        return hashtagColor;
    }

    @Override
    public int getMentionColor() {
        return mentionColor;
    }

    @Override
    public int getHyperlinkColor() {
        return hyperlinkColor;
    }

    @NonNull
    @Override
    public List<String> getHashtags() {
        return toList(createMatcher(REGEX_HASHTAG).matcher(view.getText()));
    }

    @NonNull
    @Override
    public List<String> getMentions() {
        return toList(createMatcher(REGEX_MENTION).matcher(view.getText()));
    }

    @NonNull
    @Override
    public List<String> getHyperlinks() {
        return toList(createMatcher(REGEX_HYPERLINK).matcher(view.getText()));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // triggered when text is backspaced
        if (debug)
            Log.d("beforeTextChanged", String.format("s=%s  start=%s    count=%s    after=%s", s, start, count, after));

        if (count > 0 && start > 0) {

            if (debug)
                Log.d("charAt", String.valueOf(s.charAt(start - 1)));

            switch (s.charAt(start - 1)) {
                case '#':
                    isHashtagEditing = true;
                    isMentionEditing = false;
                    break;
                case '@':
                    isHashtagEditing = false;
                    isMentionEditing = true;
                    break;
                default:
                    if (!Character.isLetterOrDigit(s.charAt(start - 1))) {
                        isHashtagEditing = false;
                        isMentionEditing = false;
                    } else if (watcher != null && isHashtagEditing) {
                        watcher.onTextChanged(view, Type.HASHTAG, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start - 1) + 1, start).toString());
                    } else if (watcher != null && isMentionEditing) {
                        watcher.onTextChanged(view, Type.MENTION, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start - 1) + 1, start).toString());
                    }
                    break;
            }
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (debug)
            Log.d("onTextChanged", String.format("s=%s  start=%s    before=%s   count=%s", s, start, before, count));

        if (s.length() > 0) {
            final Spannable spannable = (Spannable) view.getText();
            for (CharacterStyle style : spannable.getSpans(0, s.length(), CharacterStyle.class))
                spannable.removeSpan(style);
            colorize(spannable);

            // triggered when text is added
            if (start < s.length()) {

                if (debug)
                    Log.d("charAt", String.valueOf(s.charAt(start + count - 1)));

                switch (s.charAt(start + count - 1)) {
                    case '#':
                        isHashtagEditing = true;
                        isMentionEditing = false;
                        break;
                    case '@':
                        isHashtagEditing = false;
                        isMentionEditing = true;
                        break;
                    default:
                        if (!Character.isLetterOrDigit(s.charAt(start))) {
                            isHashtagEditing = false;
                            isMentionEditing = false;
                        } else if (watcher != null && isHashtagEditing) {
                            watcher.onTextChanged(view, Type.HASHTAG, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start) + 1, start + count).toString());
                        } else if (watcher != null && isMentionEditing) {
                            watcher.onTextChanged(view, Type.MENTION, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start) + 1, start + count).toString());
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
        if (hashtagEnabled) {
            Matcher matcher = createMatcher(REGEX_HASHTAG).matcher(text);
            while (matcher.find())
                ((Spannable) text).setSpan(createSpan(hashtagColor, Type.HASHTAG), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (mentionEnabled) {
            Matcher matcher = createMatcher(REGEX_MENTION).matcher(text);
            while (matcher.find())
                ((Spannable) text).setSpan(createSpan(mentionColor, Type.MENTION), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (hyperlinkEnabled) {
            Matcher matcher = createMatcher(REGEX_HYPERLINK).matcher(text);
            while (matcher.find())
                ((Spannable) text).setSpan(createSpan(hyperlinkColor, Type.HYPERLINK), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
    private CharacterStyle createSpan(int color, @NonNull final Type type) {
        if (listener == null)
            return new ForegroundColorSpan(color);
        return new SocialSpan(color) {
            @Override
            void onClick(@NonNull TextView v, @NonNull CharSequence s) {
                listener.onClick(v, type, s);
            }
        };
    }

    @NonNull
    private static List<String> toList(@NonNull Matcher matcher) {
        List<String> list = new ArrayList<>();
        while (matcher.find())
            list.add(matcher.group(1));
        return list;
    }

    @NonNull
    private static Pattern createMatcher(@NonNull String regex) {
        switch (regex) {
            case REGEX_HASHTAG:
                if (pattern_hashtag != null && pattern_hashtag.get() != null)
                    return pattern_hashtag.get();
                pattern_hashtag = new WeakReference<>(Pattern.compile(regex));
                return createMatcher(regex);
            case REGEX_MENTION:
                if (pattern_mention != null && pattern_mention.get() != null)
                    return pattern_mention.get();
                pattern_mention = new WeakReference<>(Pattern.compile(regex));
                return createMatcher(regex);
            default:
                if (pattern_hyperlink != null && pattern_hyperlink.get() != null)
                    return pattern_hyperlink.get();
                pattern_hyperlink = new WeakReference<>(Pattern.compile(regex));
                return createMatcher(regex);
        }
    }

    @NonNull
    public static SocialView attach(@NonNull TextView view) {
        return attach(view, view.getContext(), null);
    }

    @NonNull
    public static SocialView attach(@NonNull TextView view, @NonNull Context context, @Nullable AttributeSet attrs) {
        return new SocialViewAttacher(view, context, attrs);
    }

    public static void setDebug(boolean debug) {
        SocialViewAttacher.debug = debug;
    }

    private static abstract class SocialSpan extends ClickableSpan {
        private final int color;

        private SocialSpan(@ColorInt int color) {
            this.color = color;
        }

        abstract void onClick(@NonNull TextView v, @NonNull CharSequence s);

        @Override
        public void onClick(View widget) {
            TextView textView = (TextView) widget;
            Spanned spanned = (Spanned) textView.getText();
            onClick(textView, spanned.subSequence(
                    spanned.getSpanStart(this) + 1,
                    spanned.getSpanEnd(this)
            ));
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(color);
        }
    }
}