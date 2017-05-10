package com.hendraanggrian.socialview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
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
import android.util.SparseIntArray;
import android.view.View;
import android.widget.TextView;

import com.hendraanggrian.commons.content.Themes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public final class SocialViewAttacher implements SocialView, TextWatcher {

    private static final String TAG = SocialViewAttacher.class.getSimpleName();
    @Nullable private static SparseArray<Pattern> patterns;
    private static boolean debug;

    @NonNull private final TextView view;
    @NonNull private final SparseIntArray colors;
    private int underlinedFlag;
    @Nullable private OnSocialClickListener listener;
    @Nullable private SocialTextWatcher watcher;

    private boolean isHashtagEditing;
    private boolean isMentionEditing;

    private SocialViewAttacher(@NonNull TextView view, @NonNull Context context, @Nullable AttributeSet attrs) {
        this.view = view;
        this.view.setText(view.getText(), TextView.BufferType.SPANNABLE);
        this.view.addTextChangedListener(this);
        this.colors = new SparseIntArray(0);
        int defaultColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                ? Themes.getColor(view.getContext(), android.R.attr.colorAccent, view.getLinkTextColors().getDefaultColor())
                : view.getLinkTextColors().getDefaultColor();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SocialView, 0, 0);
        try {
            int enabled = a.getInteger(R.styleable.SocialView_feature, TYPE_HASHTAG | TYPE_MENTION | TYPE_HYPERLINK);
            underlinedFlag = a.getInteger(R.styleable.SocialView_featureUnderlined, TYPE_HYPERLINK);
            if ((enabled | TYPE_HASHTAG) == enabled)
                colors.put(TYPE_HASHTAG, a.getColor(R.styleable.SocialView_hashtagColor, defaultColor));
            if ((enabled | TYPE_MENTION) == enabled)
                colors.put(TYPE_MENTION, a.getColor(R.styleable.SocialView_mentionColor, defaultColor));
            if ((enabled | TYPE_HYPERLINK) == enabled)
                colors.put(TYPE_HYPERLINK, a.getColor(R.styleable.SocialView_hyperlinkColor, defaultColor));
        } finally {
            a.recycle();
            colorize();
        }
    }

    @Override
    public void setHashtagEnabled(boolean enabled) {
        if (enabled)
            colors.put(TYPE_HASHTAG, Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                    ? Themes.getColor(view.getContext(), android.R.attr.colorAccent, view.getLinkTextColors().getDefaultColor())
                    : view.getLinkTextColors().getDefaultColor());
        else
            colors.delete(TYPE_HASHTAG);
        colorize();
    }

    @Override
    public void setMentionEnabled(boolean enabled) {
        if (enabled)
            colors.put(TYPE_MENTION, Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                    ? Themes.getColor(view.getContext(), android.R.attr.colorAccent, view.getLinkTextColors().getDefaultColor())
                    : view.getLinkTextColors().getDefaultColor());
        else
            colors.delete(TYPE_MENTION);
        colorize();
    }

    @Override
    public void setHyperlinkEnabled(boolean enabled) {
        if (enabled)
            colors.put(TYPE_HYPERLINK, Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                    ? Themes.getColor(view.getContext(), android.R.attr.colorAccent, view.getLinkTextColors().getDefaultColor())
                    : view.getLinkTextColors().getDefaultColor());
        else
            colors.delete(TYPE_HYPERLINK);
        colorize();
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

    @Override
    public void setHashtagColor(@ColorInt int color) {
        colors.put(TYPE_HASHTAG, color);
        colorize();
    }

    @Override
    public void setMentionColor(@ColorInt int color) {
        colors.put(TYPE_MENTION, color);
        colorize();
    }

    @Override
    public void setHyperlinkColor(@ColorInt int color) {
        colors.put(TYPE_HYPERLINK, color);
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

    @Override
    public boolean isHashtagEnabled() {
        return colors.indexOfKey(TYPE_HASHTAG) >= 0;
    }

    @Override
    public boolean isMentionEnabled() {
        return colors.indexOfKey(TYPE_MENTION) >= 0;
    }

    @Override
    public boolean isHyperlinkEnabled() {
        return colors.indexOfKey(TYPE_HYPERLINK) >= 0;
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

    @ColorInt
    @Override
    public int getHashtagColor() {
        return colors.get(TYPE_HASHTAG);
    }

    @ColorInt
    @Override
    public int getMentionColor() {
        return colors.get(TYPE_MENTION);
    }

    @ColorInt
    @Override
    public int getHyperlinkColor() {
        return colors.get(TYPE_HYPERLINK);
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
                        watcher.onTextChanged(view, TYPE_HASHTAG, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start - 1) + 1, start).toString());
                    } else if (watcher != null && isMentionEditing) {
                        watcher.onTextChanged(view, TYPE_MENTION, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start - 1) + 1, start).toString());
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
                            watcher.onTextChanged(view, TYPE_HASHTAG, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start) + 1, start + count).toString());
                        } else if (watcher != null && isMentionEditing) {
                            watcher.onTextChanged(view, TYPE_MENTION, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start) + 1, start + count).toString());
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
        for (int i = 0; i < colors.size(); i++) {
            boolean underlined = (underlinedFlag | colors.keyAt(i)) == underlinedFlag;
            Matcher matcher = getPattern(colors.keyAt(i)).matcher(text);
            while (matcher.find()) {
                ((Spannable) text).setSpan(newSpan(colors.get(colors.keyAt(i)), underlined, colors.keyAt(i)), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
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
                listener.onClick(textView, type, spanned.subSequence(
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
     * Attach SocialView in custom view's class.
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
}