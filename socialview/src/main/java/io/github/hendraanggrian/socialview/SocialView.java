package io.github.hendraanggrian.socialview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
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
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Observable;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public final class SocialView implements SocialViewBase, TextWatcher {

    @Nullable private static Pattern PATTERN_HASHTAG, PATTERN_MENTION;

    @NonNull private final TextView view;

    private int hashtagColor, mentionColor;
    private boolean hashtagEnabled, mentionEnabled;

    @Nullable private OnSocialClickListener hashtagListener, mentionListener;

    SocialView(@NonNull TextView view, @NonNull Context context) {
        this.view = view;
        this.hashtagColor = getDefaultColor(context);
        this.mentionColor = getDefaultColor(context);
        this.hashtagEnabled = true;
        this.mentionEnabled = true;
        view.addTextChangedListener(this);
        refresh();
    }

    SocialView(@NonNull TextView view, @NonNull Context context, @NonNull AttributeSet attrs) {
        this.view = view;
        final TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SocialTextView, 0, 0);
        this.hashtagColor = array.getColor(R.styleable.SocialTextView_hashtagColor, getDefaultColor(context));
        this.mentionColor = array.getColor(R.styleable.SocialTextView_mentionColor, getDefaultColor(context));
        this.hashtagEnabled = array.getBoolean(R.styleable.SocialTextView_hashtagEnabled, true);
        this.mentionEnabled = array.getBoolean(R.styleable.SocialTextView_mentionEnabled, true);
        array.recycle();
        view.addTextChangedListener(this);
        refresh();
    }

    @Override
    public void setHashtagColor(@ColorInt int color) {
        this.hashtagColor = color;
    }

    @Override
    public void setHashtagColorRes(@ColorRes int colorRes) {
        this.hashtagColor = ContextCompat.getColor(view.getContext(), colorRes);
    }

    @Override
    public void setMentionColor(@ColorInt int color) {
        this.mentionColor = color;
    }

    @Override
    public void setMentionColorRes(@ColorRes int colorRes) {
        this.mentionColor = ContextCompat.getColor(view.getContext(), colorRes);
    }

    @Override
    public void setHashtagEnabled(boolean enabled) {
        this.hashtagEnabled = enabled;
    }

    @Override
    public void setMentionEnabled(boolean enabled) {
        this.mentionEnabled = enabled;
    }

    @Override
    public void setOnHashtagClickListener(@Nullable OnSocialClickListener listener) {
        this.hashtagListener = listener;
        refresh();
    }

    @Override
    public void setOnMentionClickListener(@Nullable OnSocialClickListener listener) {
        this.mentionListener = listener;
        refresh();
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
    public boolean isHashtagEnabled() {
        return hashtagEnabled;
    }

    @Override
    public boolean isMentionEnabled() {
        return mentionEnabled;
    }

    @NonNull
    @Override
    public List<String> getHashtags() {
        if (PATTERN_HASHTAG == null)
            PATTERN_HASHTAG = Pattern.compile("#(\\w+)");
        return extract(PATTERN_HASHTAG);
    }

    @NonNull
    @Override
    public List<String> getMentions() {
        if (PATTERN_MENTION == null)
            PATTERN_MENTION = Pattern.compile("@(\\w+)");
        return extract(PATTERN_MENTION);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Observable.just(view)
                .filter(view -> s.length() > 0)
                .map(TextView::getText)
                .map(text -> (Spannable) text)
                .subscribe(
                        spannable -> Observable.from(spannable.getSpans(0, s.length(), CharacterStyle.class)).forEach(spannable::removeSpan),
                        throwable -> {
                        }, () -> colorize(s));
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @ColorInt
    private int getDefaultColor(@NonNull Context context) {
        final TypedValue value = new TypedValue();
        return context.getTheme().resolveAttribute(R.attr.colorAccent, value, true)
                ? value.data
                : view.getCurrentTextColor();
    }

    private void refresh() {
        view.setText(view.getText(), TextView.BufferType.SPANNABLE);
        if (hashtagListener != null || mentionListener != null) {
            view.setMovementMethod(LinkMovementMethod.getInstance());
            view.setHighlightColor(Color.TRANSPARENT);
        }
        colorize(view.getText());
    }

    private void colorize(CharSequence text) {
        int startIndexOfNextHashSign;
        int index = 0;
        while (index < text.length() - 1) {
            char sign = text.charAt(index);
            int nextNotLetterDigitCharIndex = index + 1; // we assume it is next. if if was not changed by findNextValidHashTagChar then index will be incremented by 1
            if (sign == '#' && hashtagEnabled) {
                startIndexOfNextHashSign = index;
                nextNotLetterDigitCharIndex = findNextValidHashTagChar(text, startIndexOfNextHashSign);
                ((Spannable) text).setSpan(hashtagListener != null
                                ? new ClickableForegroundColorSpan(hashtagColor, hashtagListener)
                                : new ForegroundColorSpan(hashtagColor)
                        , startIndexOfNextHashSign, nextNotLetterDigitCharIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (sign == '@' && mentionEnabled) {
                startIndexOfNextHashSign = index;
                nextNotLetterDigitCharIndex = findNextValidHashTagChar(text, startIndexOfNextHashSign);
                ((Spannable) text).setSpan(mentionListener != null
                                ? new ClickableForegroundColorSpan(mentionColor, mentionListener)
                                : new ForegroundColorSpan(mentionColor)
                        , startIndexOfNextHashSign, nextNotLetterDigitCharIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            index = nextNotLetterDigitCharIndex;
        }
    }

    private int findNextValidHashTagChar(CharSequence text, int start) {
        int nonLetterDigitCharIndex = -1; // skip first sign '#"
        for (int index = start + 1; index < text.length(); index++) {
            if (!Character.isLetterOrDigit(text.charAt(index))) {
                nonLetterDigitCharIndex = index;
                break;
            }
        }
        if (nonLetterDigitCharIndex == -1)
            // we didn't find non-letter. We are at the end of text
            nonLetterDigitCharIndex = text.length();
        return nonLetterDigitCharIndex;
    }

    @NonNull
    private List<String> extract(Pattern pattern) {
        final List<String> list = new ArrayList<>();
        final Matcher matcher = pattern.matcher(view.getText().toString());
        while (matcher.find())
            list.add(matcher.group(1));
        return list;
    }

    public interface OnSocialClickListener {

        void onClick(View view, String clicked);
    }

    public static SocialView attach(@NonNull TextView view) {
        return new SocialView(view, view.getContext());
    }
}