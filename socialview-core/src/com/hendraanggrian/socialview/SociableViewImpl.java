package com.hendraanggrian.socialview;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
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
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;

import com.hendraanggrian.spannabletext.MultiSpannableImpl;
import com.hendraanggrian.spannabletext.SpanSupplier;
import com.hendraanggrian.support.utils.content.Themes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class SociableViewImpl<V extends TextView & SociableView> implements TextWatcher, SociableView {

    private static final int TYPE_HASHTAG = 1;
    private static final int TYPE_MENTION = 2;
    private static final int TYPE_HYPERLINK = 4;
    static Pattern PATTERN_HASHTAG = Pattern.compile("(?i)#([0-9A-Z_À-ÖØ-öø-ÿ]*[A-Z_]+[a-z0-9_üÀ-ÖØ-öø-ÿ]*)");
    static Pattern PATTERN_MENTION = Pattern.compile("(?i)@([0-9A-Z_À-ÖØ-öø-ÿ]*[A-Z_]+[a-z0-9_üÀ-ÖØ-öø-ÿ]*)");

    private final V view;
    private int enabledFlag;
    private ColorStateList hashtagColor;
    private ColorStateList mentionColor;
    private ColorStateList hyperlinkColor;
    @Nullable private OnSocialClickListener hashtagListener;
    @Nullable private OnSocialClickListener mentionListener;
    @Nullable private SocialTextWatcher hashtagWatcher;
    @Nullable private SocialTextWatcher mentionWatcher;

    private boolean isHashtagEditing;
    private boolean isMentionEditing;

    public SociableViewImpl(@NonNull V view, @Nullable AttributeSet attrs) {
        this.view = view;
        this.view.addTextChangedListener(this);
        this.view.setText(view.getText(), TextView.BufferType.SPANNABLE);
        TypedArray a = view.getContext().obtainStyledAttributes(attrs, R.styleable.SocialView, 0, R.style.Widget_SocialView);
        this.enabledFlag = a.getInteger(R.styleable.SocialView_socialEnabled, TYPE_HASHTAG | TYPE_MENTION | TYPE_HYPERLINK);
        this.hashtagColor = a.getColorStateList(R.styleable.SocialView_hashtagColor);
        this.mentionColor = a.getColorStateList(R.styleable.SocialView_mentionColor);
        this.hyperlinkColor = a.getColorStateList(R.styleable.SocialView_hyperlinkColor);
        a.recycle();
        colorize();
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
    public ColorStateList getHashtagColor() {
        return hashtagColor;
    }

    @Override
    public ColorStateList getMentionColor() {
        return mentionColor;
    }

    @Override
    public ColorStateList getHyperlinkColor() {
        return hyperlinkColor;
    }

    @Override
    public void setHashtagColor(@ColorInt int color) {
        hashtagColor = ColorStateList.valueOf(color);
        colorize();
    }

    @Override
    public void setMentionColor(@ColorInt int color) {
        mentionColor = ColorStateList.valueOf(color);
        colorize();
    }

    @Override
    public void setHyperlinkColor(@ColorInt int color) {
        hyperlinkColor = ColorStateList.valueOf(color);
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
        setHashtagColor(Themes.getColor(view.getContext(), colorAttr));
    }

    @Override
    public void setMentionColorAttr(@AttrRes int colorAttr) {
        setMentionColor(Themes.getColor(view.getContext(), colorAttr));
    }

    @Override
    public void setHyperlinkColorAttr(@AttrRes int colorAttr) {
        setHyperlinkColor(Themes.getColor(view.getContext(), colorAttr));
    }

    @Override
    public void setOnHashtagClickListener(@Nullable OnSocialClickListener listener) {
        if (view.getMovementMethod() == null || !(view.getMovementMethod() instanceof LinkMovementMethod)) {
            view.setMovementMethod(LinkMovementMethod.getInstance());
        }
        hashtagListener = listener;
        colorize();
    }

    @Override
    public void setOnMentionClickListener(@Nullable OnSocialClickListener listener) {
        if (view.getMovementMethod() == null || !(view.getMovementMethod() instanceof LinkMovementMethod)) {
            view.setMovementMethod(LinkMovementMethod.getInstance());
        }
        mentionListener = listener;
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
        if (!isHashtagEnabled()) {
            return Collections.emptyList();
        }
        return listOf(view.getText(), PATTERN_HASHTAG);
    }

    @NonNull
    @Override
    public Collection<String> getMentions() {
        if (!isMentionEnabled()) {
            return Collections.emptyList();
        }
        return listOf(view.getText(), PATTERN_MENTION);
    }

    @NonNull
    @Override
    public Collection<String> getHyperlinks() {
        if (!isHyperlinkEnabled()) {
            return Collections.emptyList();
        }
        return listOf(view.getText(), Patterns.WEB_URL);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (count > 0 && start > 0) {
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
        if (s.length() > 0) {
            colorize();
            // triggered when text is added
            if (start < s.length()) {
                if (start + count - 1 < 0) {
                    return;
                }
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
                            hashtagWatcher.onSocialTextChanged(SociableViewImpl.this, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start) + 1, start + count).toString());
                        } else if (mentionWatcher != null && isMentionEditing) {
                            mentionWatcher.onSocialTextChanged(SociableViewImpl.this, s.subSequence(indexOfPreviousNonLetterDigit(s, 0, start) + 1, start + count).toString());
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
        for (int i = start + 1; i < text.length(); i++) {
            if (!Character.isLetterOrDigit(text.charAt(i))) {
                return i;
            }
        }
        return text.length();
    }

    private int indexOfPreviousNonLetterDigit(CharSequence text, int start, int end) {
        for (int i = end; i > start; i--) {
            if (!Character.isLetterOrDigit(text.charAt(i))) {
                return i;
            }
        }
        return start;
    }

    private void colorize() {
        final CharSequence text = view.getText();
        if (!(text instanceof Spannable)) {
            throw new IllegalStateException("Attached text is not a Spannable, add TextView.BufferType.SPANNABLE when setting text to this TextView.");
        }
        final Spannable spannable = (Spannable) text;
        // remove all spans
        for (CharacterStyle span : spannable.getSpans(0, spannable.length(), CharacterStyle.class)) {
            spannable.removeSpan(span);
        }
        // refill new spans
        MultiSpannableImpl impl = new MultiSpannableImpl(spannable);
        if (isHashtagEnabled()) {
            impl.putSpansAll(PATTERN_HASHTAG, new SpanSupplier() {
                @NonNull
                @Override
                public Object getSpan() {
                    if (hashtagListener == null) {
                        return new ForegroundColorSpan(hashtagColor.getDefaultColor());
                    }
                    return new ForegroundColorClickableSpan(hashtagColor.getDefaultColor()) {
                        @Override
                        public void onClick(View widget) {
                            hashtagListener.onSocialClick(view, spannable.subSequence(spannable.getSpanStart(this) + 1, spannable.getSpanEnd(this)));
                        }
                    };
                }
            });
        }
        if (isMentionEnabled()) {
            impl.putSpansAll(PATTERN_MENTION, new SpanSupplier() {
                @NonNull
                @Override
                public Object getSpan() {
                    if (mentionListener == null) {
                        return new ForegroundColorSpan(mentionColor.getDefaultColor());
                    }
                    return new ForegroundColorClickableSpan(mentionColor.getDefaultColor()) {
                        @Override
                        public void onClick(View widget) {
                            mentionListener.onSocialClick(view, spannable.subSequence(spannable.getSpanStart(this) + 1, spannable.getSpanEnd(this)));
                        }
                    };
                }
            });
        }
        if (isHyperlinkEnabled()) {
            impl.putSpansAll(Patterns.WEB_URL, new SpanSupplier() {
                @NonNull
                @Override
                public Object getSpan() {
                    return new SimpleURLSpan(spannable, hyperlinkColor.getDefaultColor());
                }
            });
        }
    }

    @NonNull
    static List<String> listOf(@NonNull CharSequence input, @NonNull Pattern pattern) {
        Matcher matcher = pattern.matcher(input);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group(pattern != Patterns.WEB_URL
                    ? 1 // remove hashtag and mention symbol
                    : 0));
        }
        return list;
    }

    public static void setHashtagPattern(@NonNull String regex) {
        PATTERN_HASHTAG = Pattern.compile(regex);
    }

    public static void setMentionPattern(@NonNull String regex) {
        PATTERN_MENTION = Pattern.compile(regex);
    }
}