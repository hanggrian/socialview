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
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;

import com.hendraanggrian.support.utils.content.Themes;
import com.hendraanggrian.support.utils.text.Spannables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Hendra Anggrian (com.hendraanggrian@gmail.com)
 */
public final class SociableViewImpl implements com.hendraanggrian.socialview.SociableView, TextWatcher {

    private static final String TAG = "SocialView";
    private static final int TYPE_HASHTAG = 1;
    private static final int TYPE_MENTION = 2;
    private static final int TYPE_HYPERLINK = 4;
    private static final Pattern PATTERN_HASHTAG = Pattern.compile("#(\\w+)");
    private static final Pattern PATTERN_MENTION = Pattern.compile("@(\\w+)");
    private static boolean DEBUG;

    @NonNull private final TextView view;
    @NonNull private final Collection<Object> allSpans;
    private int enabledFlag;
    @ColorInt private int hashtagColor;
    @ColorInt private int mentionColor;
    @ColorInt private int hyperlinkColor;
    @Nullable private com.hendraanggrian.socialview.OnSocialClickListener hashtagListener;
    @Nullable private com.hendraanggrian.socialview.OnSocialClickListener mentionListener;
    @Nullable private com.hendraanggrian.socialview.SocialTextWatcher hashtagWatcher;
    @Nullable private com.hendraanggrian.socialview.SocialTextWatcher mentionWatcher;

    private boolean isHashtagEditing;
    private boolean isMentionEditing;

    private SociableViewImpl(@NonNull TextView view, @NonNull Context context, @Nullable AttributeSet attrs) {
        this.view = view;
        this.view.setText(view.getText(), TextView.BufferType.SPANNABLE);
        this.view.addTextChangedListener(this);
        this.allSpans = new ArrayList<>();
        int defaultColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                ? Themes.getColor(view.getContext(), android.R.attr.colorAccent, view.getLinkTextColors().getDefaultColor())
                : view.getLinkTextColors().getDefaultColor();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SocialView, 0, 0);
        try {
            enabledFlag = a.getInteger(R.styleable.SocialView_socialEnabled, TYPE_HASHTAG | TYPE_MENTION | TYPE_HYPERLINK);
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
    public void setOnHashtagClickListener(@Nullable OnSocialClickListener listener) {
        hashtagListener = listener;
        view.setMovementMethod(LinkMovementMethod.getInstance());
        colorize();
    }

    @Override
    public void setOnMentionClickListener(@Nullable OnSocialClickListener listener) {
        mentionListener = listener;
        view.setMovementMethod(LinkMovementMethod.getInstance());
        colorize();
    }

    @Override
    public void setHashtagTextChangedListener(@Nullable SocialTextWatcher watcher) {
        hashtagWatcher = watcher;
    }

    @Override
    public void setMentionTextChangedListener(@Nullable SocialTextWatcher watcher) {
        mentionWatcher = watcher;
    }

    @NonNull
    @Override
    public Collection<String> getHashtags() {
        if (!isHashtagEnabled())
            return Collections.emptyList();
        return newList(PATTERN_HASHTAG);
    }

    @NonNull
    @Override
    public Collection<String> getMentions() {
        if (!isMentionEnabled())
            return Collections.emptyList();
        return newList(PATTERN_MENTION);
    }

    @NonNull
    @Override
    public Collection<String> getHyperlinks() {
        if (!isHyperlinkEnabled())
            return Collections.emptyList();
        return newList(Patterns.WEB_URL);
    }

    @NonNull
    private List<String> newList(@NonNull Pattern pattern) {
        Matcher matcher = pattern.matcher(view.getText());
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            // remove hashtag and mention symbol
            list.add(matcher.group(pattern == Patterns.WEB_URL ? 0 : 1));
        }
        return list;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // triggered when text is backspaced
        if (DEBUG)
            Log.d(TAG, String.format("beforeTextChanged s=%s  start=%s    count=%s    after=%s", s, start, count, after));

        if (count > 0 && start > 0) {

            if (DEBUG)
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
                    } else if (hashtagWatcher != null && isHashtagEditing) {
                        hashtagWatcher.onSocialTextChanged(view, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start - 1) + 1, start).toString());
                    } else if (mentionWatcher != null && isMentionEditing) {
                        mentionWatcher.onSocialTextChanged(view, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start - 1) + 1, start).toString());
                    }
                    break;
            }
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (DEBUG)
            Log.d(TAG, String.format("onSocialTextChanged s=%s  start=%s    before=%s   count=%s", s, start, before, count));

        if (s.length() > 0) {
            Spannable spannable = (Spannable) view.getText();
            for (CharacterStyle style : spannable.getSpans(0, s.length(), CharacterStyle.class))
                spannable.removeSpan(style);
            colorize(spannable);

            // triggered when text is added
            if (start < s.length()) {

                if (DEBUG)
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
                        } else if (hashtagWatcher != null && isHashtagEditing) {
                            hashtagWatcher.onSocialTextChanged(view, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start) + 1, start + count).toString());
                        } else if (mentionWatcher != null && isMentionEditing) {
                            mentionWatcher.onSocialTextChanged(view, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start) + 1, start + count).toString());
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
        colorize((Spannable) view.getText());
    }

    private void colorize(@NonNull final Spannable text) {
        // clear all allSpans
        Spannables.removeSpans(text, allSpans.toArray());
        allSpans.clear();
        // refill text with new allSpans
        if (isHashtagEnabled()) {
            allSpans.addAll(Spannables.putSpansAll(text, PATTERN_HASHTAG, new Spannables.SpanGetter() {
                @NonNull
                @Override
                public Object getSpan() {
                    return hashtagListener == null
                            ? new ForegroundColorSpan(hashtagColor)
                            : new com.hendraanggrian.socialview.ForegroundColorClickableSpan(hashtagColor) {
                        @Override
                        public void onClick(View widget) {
                            hashtagListener.onSocialClick(view, text.subSequence(text.getSpanStart(this) + 1, text.getSpanEnd(this)).toString());
                        }
                    };
                }
            }));
        }
        if (isMentionEnabled()) {
            allSpans.addAll(Spannables.putSpansAll(text, PATTERN_MENTION, new Spannables.SpanGetter() {
                @NonNull
                @Override
                public Object getSpan() {
                    return mentionListener == null
                            ? new ForegroundColorSpan(mentionColor)
                            : new com.hendraanggrian.socialview.ForegroundColorClickableSpan(mentionColor) {
                        @Override
                        public void onClick(View widget) {
                            mentionListener.onSocialClick(view, text.subSequence(text.getSpanStart(this) + 1, text.getSpanEnd(this)).toString());
                        }
                    };
                }
            }));
        }
        if (isHyperlinkEnabled()) {
            allSpans.addAll(Spannables.putSpansAll(text, Patterns.WEB_URL, new Spannables.SpanGetter() {
                @NonNull
                @Override
                public Object getSpan() {
                    return new URLSpan("") {
                        @Override
                        public String getURL() {
                            return text.subSequence(text.getSpanStart(this), text.getSpanEnd(this)).toString();
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            ds.linkColor = hyperlinkColor;
                            super.updateDrawState(ds);
                        }
                    };
                }
            }));
        }
    }

    public static void setDebug(boolean debug) {
        SociableViewImpl.DEBUG = debug;
    }

    /**
     * Attach SocialView to any TextView or its subclasses.
     */
    @NonNull
    public static com.hendraanggrian.socialview.SociableView attach(@NonNull TextView view) {
        return attach(view, view.getContext(), null);
    }

    /**
     * Attach SocialView in custom view's class.
     */
    @NonNull
    public static com.hendraanggrian.socialview.SociableView attach(@NonNull TextView view, @NonNull Context context, @Nullable AttributeSet attrs) {
        return new SociableViewImpl(view, context, attrs);
    }
}