package com.hendraanggrian.socialview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.hendraanggrian.compat.content.Themes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public final class SocialView implements SociableView, TextWatcher {

    private static final int TYPE_HASHTAG = 1;
    private static final int TYPE_MENTION = 2;
    private static final int TYPE_HYPERLINK = 4;

    @IntDef({TYPE_HASHTAG, TYPE_MENTION, TYPE_HYPERLINK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }

    private static final String TAG = "SocialView";
    @Nullable private static SparseArray<Pattern> patterns;
    private static boolean debug;

    @NonNull private final TextView view;
    private int enabledFlag;
    private int underlinedFlag;
    @ColorInt private int hashtagColor;
    @ColorInt private int mentionColor;
    @ColorInt private int hyperlinkColor;
    @Nullable private OnSocialClickListener listener;
    @Nullable private SocialTextWatcher watcher;

    private boolean isHashtagEditing;
    private boolean isMentionEditing;

    private SocialView(@NonNull TextView view, @NonNull Context context, @Nullable AttributeSet attrs) {
        this.view = view;
        this.view.setText(view.getText(), TextView.BufferType.SPANNABLE);
        this.view.addTextChangedListener(this);
        int defaultColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                ? Themes.getColor(view.getContext(), android.R.attr.colorAccent, view.getLinkTextColors().getDefaultColor())
                : view.getLinkTextColors().getDefaultColor();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SocialView, 0, 0);
        try {
            enabledFlag = a.getInteger(R.styleable.SocialView_socialEnabled, TYPE_HASHTAG | TYPE_MENTION | TYPE_HYPERLINK);
            underlinedFlag = a.getInteger(R.styleable.SocialView_socialUnderlined, TYPE_HYPERLINK);
            hashtagColor = a.getColor(R.styleable.SocialView_hashtagColor, defaultColor);
            mentionColor = a.getColor(R.styleable.SocialView_mentionColor, defaultColor);
            hyperlinkColor = a.getColor(R.styleable.SocialView_hyperlinkColor, defaultColor);
        } finally {
            a.recycle();
            colorize();
        }
    }

    @Override
    public boolean isHashtagEnabled() {
        return (enabledFlag | TYPE_HASHTAG) == enabledFlag;
    }

    @Override
    public boolean isMentionEnabled() {
        return (enabledFlag | TYPE_MENTION) == enabledFlag;
    }

    @Override
    public boolean isHyperlinkEnabled() {
        return (enabledFlag | TYPE_HYPERLINK) == enabledFlag;
    }

    @Override
    public void setHashtagEnabled(boolean enabled) {
        enabledFlag = enabled
                ? enabledFlag | TYPE_HASHTAG
                : enabledFlag & (~TYPE_HASHTAG);
        colorize();
    }

    @Override
    public void setMentionEnabled(boolean enabled) {
        enabledFlag = enabled
                ? enabledFlag | TYPE_MENTION
                : enabledFlag & (~TYPE_MENTION);
        colorize();
    }

    @Override
    public void setHyperlinkEnabled(boolean enabled) {
        enabledFlag = enabled
                ? enabledFlag | TYPE_HYPERLINK
                : enabledFlag & (~TYPE_HYPERLINK);
        colorize();
    }

    @Override
    public boolean isHashtagUnderlined() {
        return isHashtagEnabled() && (underlinedFlag | TYPE_HASHTAG) == underlinedFlag;
    }

    @Override
    public boolean isMentionUnderlined() {
        return isMentionEnabled() && (underlinedFlag | TYPE_MENTION) == underlinedFlag;
    }

    @Override
    public boolean isHyperlinkUnderlined() {
        return isHyperlinkEnabled() && (underlinedFlag | TYPE_HYPERLINK) == underlinedFlag;
    }

    @Override
    public void setHashtagUnderlined(boolean underlined) {
        underlinedFlag = underlined
                ? underlinedFlag | TYPE_HASHTAG
                : underlinedFlag & (~TYPE_HASHTAG);
        colorize();
    }

    @Override
    public void setMentionUnderlined(boolean underlined) {
        underlinedFlag = underlined
                ? underlinedFlag | TYPE_MENTION
                : underlinedFlag & (~TYPE_MENTION);
        colorize();
    }

    @Override
    public void setHyperlinkUnderlined(boolean underlined) {
        underlinedFlag = underlined
                ? underlinedFlag | TYPE_HYPERLINK
                : underlinedFlag & (~TYPE_HYPERLINK);
        colorize();
    }

    @ColorInt
    @Override
    public int getHashtagColor() {
        return hashtagColor;
    }

    @ColorInt
    @Override
    public int getMentionColor() {
        return mentionColor;
    }

    @ColorInt
    @Override
    public int getHyperlinkColor() {
        return hyperlinkColor;
    }

    @Override
    public void setHashtagColor(@ColorInt int color) {
        hashtagColor = color;
        colorize();
    }

    @Override
    public void setMentionColor(@ColorInt int color) {
        mentionColor = color;
        colorize();
    }

    @Override
    public void setHyperlinkColor(@ColorInt int color) {
        hyperlinkColor = color;
        colorize();
    }

    @Override
    public void setHashtagColorRes(@ColorRes int colorRes) {
        setHashtagColor(ContextCompat.getColor(view.getContext(), colorRes));
    }

    @Override
    public void setMentionColorRes(@ColorRes int colorRes) {
        setMentionColor(ContextCompat.getColor(view.getContext(), colorRes));
    }

    @Override
    public void setHyperlinkColorRes(@ColorRes int colorRes) {
        setHyperlinkColor(ContextCompat.getColor(view.getContext(), colorRes));
    }

    @Override
    public void setHashtagColorAttr(@AttrRes int colorAttr) {
        setHashtagColor(Themes.getColor(view.getContext(), colorAttr, view.getLinkTextColors().getDefaultColor()));
    }

    @Override
    public void setMentionColorAttr(@AttrRes int colorAttr) {
        setMentionColor(Themes.getColor(view.getContext(), colorAttr, view.getLinkTextColors().getDefaultColor()));
    }

    @Override
    public void setHyperlinkColorAttr(@AttrRes int colorAttr) {
        setHyperlinkColor(Themes.getColor(view.getContext(), colorAttr, view.getLinkTextColors().getDefaultColor()));
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

    @NonNull
    @Override
    public List<String> getHashtags() {
        if (!isHashtagEnabled())
            return Collections.emptyList();
        return newList(TYPE_HASHTAG);
    }

    @NonNull
    @Override
    public List<String> getMentions() {
        if (!isMentionEnabled())
            return Collections.emptyList();
        return newList(TYPE_MENTION);
    }

    @NonNull
    @Override
    public List<String> getHyperlinks() {
        if (!isHyperlinkEnabled())
            return Collections.emptyList();
        return newList(TYPE_HYPERLINK);
    }

    @NonNull
    private List<String> newList(int type) {
        Matcher matcher = getPattern(type).matcher(view.getText());
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            // remove hashtag and mention symbol
            list.add(matcher.group(type == TYPE_HYPERLINK ? 0 : 1));
        }
        return list;
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
                        watcher.onSocialTextChanged(view, TYPE_HASHTAG, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start - 1) + 1, start).toString());
                    } else if (watcher != null && isMentionEditing) {
                        watcher.onSocialTextChanged(view, TYPE_MENTION, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start - 1) + 1, start).toString());
                    }
                    break;
            }
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (debug)
            Log.d(TAG, String.format("onSocialTextChanged s=%s  start=%s    before=%s   count=%s", s, start, before, count));

        if (s.length() > 0) {
            Spannable spannable = (Spannable) view.getText();
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
                            watcher.onSocialTextChanged(view, TYPE_HASHTAG, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start) + 1, start + count).toString());
                        } else if (watcher != null && isMentionEditing) {
                            watcher.onSocialTextChanged(view, TYPE_MENTION, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start) + 1, start + count).toString());
                        }
                        break;
                }
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
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

    private void colorize() {
        colorize(view.getText());
    }

    private void colorize(@NonNull CharSequence text) {
        boolean underlined;
        Matcher matcher;
        if (isHashtagEnabled()) {
            underlined = isHashtagUnderlined();
            matcher = getPattern(TYPE_HASHTAG).matcher(text);
            while (matcher.find())
                ((Spannable) text).setSpan(newSpan(hashtagColor, underlined, TYPE_HASHTAG), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (isMentionEnabled()) {
            underlined = isMentionUnderlined();
            matcher = getPattern(TYPE_MENTION).matcher(text);
            while (matcher.find())
                ((Spannable) text).setSpan(newSpan(mentionColor, underlined, TYPE_MENTION), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (isHyperlinkEnabled()) {
            underlined = isHyperlinkUnderlined();
            matcher = getPattern(TYPE_HYPERLINK).matcher(text);
            while (matcher.find())
                ((Spannable) text).setSpan(newSpan(hyperlinkColor, underlined, TYPE_HYPERLINK), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    @NonNull
    private CharacterStyle newSpan(int color, boolean underlined, final int type) {
        if (listener == null)
            return new ForegroundColorSpan(color);
        return new ForegroundColorClickableSpan(color, underlined) {
            @Override
            public void onClick(View widget) {
                TextView textView = (TextView) widget;
                Spanned spanned = (Spanned) textView.getText();
                listener.onSocialClick(textView, type, spanned.subSequence(
                        // remove hashtag and mention symbol
                        type == TYPE_HYPERLINK ? spanned.getSpanStart(this) : spanned.getSpanStart(this) + 1,
                        spanned.getSpanEnd(this)
                ));
            }
        };
    }

    @NonNull
    private static Pattern getPattern(int type) {
        if (patterns == null)
            patterns = new SparseArray<>(0);
        Pattern pattern = patterns.get(type);
        if (pattern != null)
            return pattern;
        else if (type == TYPE_HASHTAG)
            patterns.put(type, Pattern.compile("#(\\w+)"));
        else if (type == TYPE_MENTION)
            patterns.put(type, Pattern.compile("@(\\w+)"));
        else
            patterns.put(type, Pattern.compile("[a-z]+:\\/\\/[^ \\n]*"));
        return getPattern(type);
    }

    public static void setDebug(boolean debug) {
        SocialView.debug = debug;
    }

    /**
     * Attach SocialView to any TextView or its subclasses.
     *
     * @param view TextView to attach, can't be null.
     * @return SocialView interface.
     */
    @NonNull
    public static SociableView attach(@NonNull TextView view) {
        return attach(view, view.getContext(), null);
    }

    /**
     * Attach SocialView in custom view's class.
     *
     * @param view    TextView to attach, can't be null.
     * @param context context passed in constructor.
     * @param attrs   attributes passed by the view, might be null.
     * @return SocialView interface.
     */
    @NonNull
    public static SociableView attach(@NonNull TextView view, @NonNull Context context, @Nullable AttributeSet attrs) {
        return new SocialView(view, context, attrs);
    }
}