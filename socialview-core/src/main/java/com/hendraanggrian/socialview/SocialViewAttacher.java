package com.hendraanggrian.socialview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public final class SocialViewAttacher implements SocialView, TextWatcher {

    private static final String TAG = SocialView.class.getSimpleName();
    private static final String REGEX_HASHTAG = "#(\\w+)";
    private static final String REGEX_MENTION = "@(\\w+)";
    private static final String REGEX_HYPERLINK = "[a-z]+:\\/\\/[^ \\n]*";
    private static final int FLAG_HASHTAG = 1;
    private static final int FLAG_MENTION = 2;
    private static final int FLAG_HYPERLINK = 4;

    @Nullable private static WeakReference<Pattern> patternHashtag;
    @Nullable private static WeakReference<Pattern> patternMention;
    @Nullable private static WeakReference<Pattern> patternHyperlink;
    private static boolean debug;

    @NonNull private final TextView textView;
    private int flagEnabled;
    private int flagUnderlined;
    @ColorInt private int colorHashtag;
    @ColorInt private int colorMention;
    @ColorInt private int colorHyperlink;
    @Nullable private OnSocialClickListener listener;
    @Nullable private SocialTextWatcher watcher;

    private boolean isHashtagEditing;
    private boolean isMentionEditing;

    private SocialViewAttacher(@NonNull TextView textView, @NonNull Context context, @Nullable AttributeSet attrs) {
        this.textView = textView;
        this.textView.setText(textView.getText(), TextView.BufferType.SPANNABLE);
        this.textView.addTextChangedListener(this);
        Resources.Theme theme = context.getTheme();
        int defaultColor = !this.textView.isInEditMode()
                ? ColorUtils.getThemeAccentColor(theme, this.textView.getCurrentTextColor())
                : this.textView.getCurrentTextColor();
        TypedArray array = theme.obtainStyledAttributes(attrs, R.styleable.SocialView, 0, 0);
        try {
            flagEnabled = array.getInteger(R.styleable.SocialView_typeEnabled, FLAG_HASHTAG | FLAG_MENTION | FLAG_HYPERLINK);
            flagUnderlined = array.getInteger(R.styleable.SocialView_typeUnderlined, FLAG_HYPERLINK);
            colorHashtag = array.getColor(R.styleable.SocialView_hashtagColor, defaultColor);
            colorMention = array.getColor(R.styleable.SocialView_mentionColor, defaultColor);
            colorHyperlink = array.getColor(R.styleable.SocialView_hyperlinkColor, defaultColor);
        } finally {
            array.recycle();
            colorize();
        }
    }

    @Override
    public void setHashtagEnabled(boolean enabled) {
        flagEnabled = combineFlag(flagEnabled, FLAG_HASHTAG, enabled);
        colorize();
    }

    @Override
    public void setMentionEnabled(boolean enabled) {
        flagEnabled = combineFlag(flagEnabled, FLAG_MENTION, enabled);
        colorize();
    }

    @Override
    public void setHyperlinkEnabled(boolean enabled) {
        flagEnabled = combineFlag(flagEnabled, FLAG_HYPERLINK, enabled);
        colorize();
    }

    @Override
    public void setHashtagUnderlined(boolean underlined) {
        flagUnderlined = combineFlag(flagUnderlined, FLAG_HASHTAG, underlined);
        colorize();
    }

    @Override
    public void setMentionUnderlined(boolean underlined) {
        flagUnderlined = combineFlag(flagUnderlined, FLAG_MENTION, underlined);
        colorize();
    }

    @Override
    public void setHyperlinkUnderlined(boolean underlined) {
        flagUnderlined = combineFlag(flagUnderlined, FLAG_HYPERLINK, underlined);
        colorize();
    }

    @Override
    public void setHashtagColor(@ColorInt int color) {
        colorHashtag = color;
        colorize();
    }

    @Override
    public void setMentionColor(@ColorInt int color) {
        colorMention = color;
        colorize();
    }

    @Override
    public void setHyperlinkColor(@ColorInt int color) {
        colorHyperlink = color;
        colorize();
    }

    @Override
    public void setHashtagColorRes(@ColorRes int colorRes) {
        setHashtagColor(ColorUtils.getColor(textView.getContext(), colorRes));
    }

    @Override
    public void setMentionColorRes(@ColorRes int colorRes) {
        setMentionColor(ColorUtils.getColor(textView.getContext(), colorRes));
    }

    @Override
    public void setHyperlinkColorRes(@ColorRes int colorRes) {
        setHyperlinkColor(ColorUtils.getColor(textView.getContext(), colorRes));
    }

    @Override
    public void setOnSocialClickListener(@Nullable OnSocialClickListener listener) {
        this.listener = listener;
        this.textView.setMovementMethod(LinkMovementMethod.getInstance());
        colorize();
    }

    @Override
    public void setSocialTextChangedListener(@Nullable SocialTextWatcher watcher) {
        this.watcher = watcher;
    }

    @Override
    public boolean isHashtagEnabled() {
        return (flagEnabled | FLAG_HASHTAG) == flagEnabled;
    }

    @Override
    public boolean isMentionEnabled() {
        return (flagEnabled | FLAG_MENTION) == flagEnabled;
    }

    @Override
    public boolean isHyperlinkEnabled() {
        return (flagEnabled | FLAG_HYPERLINK) == flagEnabled;
    }

    @Override
    public boolean isHashtagUnderlined() {
        return (flagUnderlined | FLAG_HASHTAG) == flagUnderlined;
    }

    @Override
    public boolean isMentionUnderlined() {
        return (flagUnderlined | FLAG_MENTION) == flagUnderlined;
    }

    @Override
    public boolean isHyperlinkUnderlined() {
        return (flagUnderlined | FLAG_HYPERLINK) == flagUnderlined;
    }

    @ColorInt
    @Override
    public int getHashtagColor() {
        return colorHashtag;
    }

    @ColorInt
    @Override
    public int getMentionColor() {
        return colorMention;
    }

    @ColorInt
    @Override
    public int getHyperlinkColor() {
        return colorHyperlink;
    }

    @NonNull
    @Override
    public List<String> getHashtags() {
        if (!isHashtagEnabled())
            return Collections.emptyList();
        return toList(REGEX_HASHTAG, textView.getText());
    }

    @NonNull
    @Override
    public List<String> getMentions() {
        if (!isMentionEnabled())
            return Collections.emptyList();
        return toList(REGEX_MENTION, textView.getText());
    }

    @NonNull
    @Override
    public List<String> getHyperlinks() {
        if (!isHyperlinkEnabled())
            return Collections.emptyList();
        return toList(REGEX_HYPERLINK, textView.getText());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // triggered when text is backspaced
        if (debug)
            Log.d(TAG, String.format("beforeTextChanged s=%s  start=%s    count=%s    after=%s", s, start, count, after));

        if (count > 0 && start > 0) {

            if (debug)
                Log.d(TAG, "charAt " + String.valueOf(s.charAt(start - 1)));

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
                        watcher.onTextChanged(textView, Type.HASHTAG, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start - 1) + 1, start).toString());
                    } else if (watcher != null && isMentionEditing) {
                        watcher.onTextChanged(textView, Type.MENTION, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start - 1) + 1, start).toString());
                    }
                    break;
            }
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (debug)
            Log.d(TAG, String.format("onTextChanged s=%s  start=%s    before=%s   count=%s", s, start, before, count));

        if (s.length() > 0) {
            final Spannable spannable = (Spannable) textView.getText();
            for (CharacterStyle style : spannable.getSpans(0, s.length(), CharacterStyle.class))
                spannable.removeSpan(style);
            colorize(spannable);

            // triggered when text is added
            if (start < s.length()) {

                if (debug)
                    Log.d(TAG, "charAt " + String.valueOf(s.charAt(start + count - 1)));

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
                            watcher.onTextChanged(textView, Type.HASHTAG, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start) + 1, start + count).toString());
                        } else if (watcher != null && isMentionEditing) {
                            watcher.onTextChanged(textView, Type.MENTION, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start) + 1, start + count).toString());
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
        colorize(textView.getText());
    }

    private void colorize(@NonNull CharSequence text) {
        if (isHashtagEnabled()) {
            boolean underlined = isHashtagUnderlined();
            Matcher matcher = toPattern(REGEX_HASHTAG).matcher(text);
            while (matcher.find())
                ((Spannable) text).setSpan(createSpan(colorHashtag, underlined, Type.HASHTAG), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (isMentionEnabled()) {
            boolean underlined = isMentionUnderlined();
            Matcher matcher = toPattern(REGEX_MENTION).matcher(text);
            while (matcher.find())
                ((Spannable) text).setSpan(createSpan(colorMention, underlined, Type.MENTION), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (isHyperlinkEnabled()) {
            boolean underlined = isHyperlinkUnderlined();
            Matcher matcher = toPattern(REGEX_HYPERLINK).matcher(text);
            while (matcher.find())
                ((Spannable) text).setSpan(createSpan(colorHyperlink, underlined, Type.HYPERLINK), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
    private CharacterStyle createSpan(int color, boolean underlined, @NonNull final Type type) {
        if (listener == null)
            return new ForegroundColorSpan(color);
        return new ForegroundColorClickableSpan(color, underlined) {
            @Override
            void onClick(@NonNull TextView v, @NonNull CharSequence text) {
                // remove hashtag and mention symbol
                listener.onClick(v, type, type == Type.HYPERLINK
                        ? text
                        : text.subSequence(1, text.length()));
            }
        };
    }

    @NonNull
    private static List<String> toList(@NonNull String regex, @NonNull CharSequence input) {
        Matcher matcher = toPattern(regex).matcher(input);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            // remove hashtag and mention symbol
            list.add(matcher.group(regex.equals(REGEX_HYPERLINK) ? 0 : 1));
        }
        return list;
    }

    @NonNull
    private static Pattern toPattern(@NonNull String regex) {
        switch (regex) {
            case REGEX_HASHTAG:
                if (patternHashtag != null && patternHashtag.get() != null)
                    return patternHashtag.get();
                patternHashtag = new WeakReference<>(Pattern.compile(regex));
                break;
            case REGEX_MENTION:
                if (patternMention != null && patternMention.get() != null)
                    return patternMention.get();
                patternMention = new WeakReference<>(Pattern.compile(regex));
                break;
            case REGEX_HYPERLINK:
                if (patternHyperlink != null && patternHyperlink.get() != null)
                    return patternHyperlink.get();
                patternHyperlink = new WeakReference<>(Pattern.compile(regex));
                break;
        }
        return toPattern(regex);
    }

    public static void setDebug(boolean debug) {
        SocialViewAttacher.debug = debug;
    }

    /**
     * Attach SocialView to any TextView or its subclasses.
     *
     * @param view TextView to attach, can't be null.
     * @return SocialView interface.
     */
    @NonNull
    public static SocialView attach(@NonNull TextView view) {
        return attach(view, view.getContext(), null);
    }

    /**
     * Attach SocialView in custom views' constructors.
     *
     * @param view    TextView to attach, can't be null.
     * @param context context passed in constructor.
     * @param attrs   attributes passed by the view, might be null.
     * @return SocialView interface.
     * @see com.hendraanggrian.widget.SocialTextView
     * @see com.hendraanggrian.widget.SocialEditText
     */
    @NonNull
    public static SocialView attach(@NonNull TextView view, @NonNull Context context, @Nullable AttributeSet attrs) {
        return new SocialViewAttacher(view, context, attrs);
    }

    private static int combineFlag(int flags, int flag, boolean addFlag) {
        return addFlag
                ? flags | flag
                : flags & (~flag);
    }
}