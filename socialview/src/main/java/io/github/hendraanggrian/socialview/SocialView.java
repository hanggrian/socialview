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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public final class SocialView implements SocialViewBase, TextWatcher {

    @NonNull private final TextView view;

    private int hashtagColor;
    private int atColor;
    private boolean hashtagEnabled;
    private boolean atEnabled;
    @Nullable private OnSocialClickListener listener;

    public SocialView(@NonNull TextView view, @NonNull Context context) {
        this.view = view;
        this.hashtagColor = getDefaultColor(context);
        this.atColor = getDefaultColor(context);
        this.hashtagEnabled = true;
        this.atEnabled = true;
        view.addTextChangedListener(this);
        refresh();
    }

    public SocialView(@NonNull TextView view, @NonNull Context context, @NonNull AttributeSet attrs) {
        this.view = view;
        final TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SocialTextView, 0, 0);
        this.hashtagColor = array.getColor(R.styleable.SocialTextView_hashtagColor, getDefaultColor(context));
        this.atColor = array.getColor(R.styleable.SocialTextView_atColor, getDefaultColor(context));
        this.hashtagEnabled = array.getBoolean(R.styleable.SocialTextView_hashtagEnabled, true);
        this.atEnabled = array.getBoolean(R.styleable.SocialTextView_atEnabled, true);
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
    public void setUsernameColor(@ColorInt int color) {
        this.atColor = color;
    }

    @Override
    public void setUsernameColorRes(@ColorRes int colorRes) {
        this.atColor = ContextCompat.getColor(view.getContext(), colorRes);
    }

    @Override
    public void setHashtagEnabled(boolean enabled) {
        this.hashtagEnabled = enabled;
    }

    @Override
    public void setUsernameEnabled(boolean enabled) {
        this.atEnabled = enabled;
    }

    @Override
    public void setOnSocialClickListener(@Nullable OnSocialClickListener listener) {
        this.listener = listener;
        refresh();
    }

    @Override
    public int getHashtagColor() {
        return hashtagColor;
    }

    @Override
    public int getUsernameColor() {
        return atColor;
    }

    @Override
    public boolean isHashtagEnabled() {
        return hashtagEnabled;
    }

    @Override
    public boolean isUsernameEnabled() {
        return atEnabled;
    }

    @NonNull
    @Override
    public List<String> getHashtags() {
        return getHashtags(false);
    }

    @NonNull
    @Override
    public List<String> getHashtags(boolean withSymbol) {
        String original = view.getText().toString();
        Spannable spannable = (Spannable) view.getText();

        List<String> hashTags = new ArrayList<>();
        Observable.from(spannable.getSpans(0, original.length(), CharacterStyle.class))
                .forEach(charStyle -> hashTags.add(original.substring(!withSymbol
                        ? spannable.getSpanStart(charStyle) + 1/*skip "#" sign*/
                        : spannable.getSpanStart(charStyle), spannable.getSpanEnd(charStyle))));
        return hashTags;
    }

    @NonNull
    @Override
    public List<String> getUsernames() {
        return getUsernames(false);
    }

    @NonNull
    @Override
    public List<String> getUsernames(boolean withSymbol) {
        String original = view.getText().toString();
        Spannable spannable = (Spannable) view.getText();

        List<String> hashTags = new ArrayList<>();
        Observable.from(spannable.getSpans(0, original.length(), CharacterStyle.class))
                .forEach(charStyle -> hashTags.add(original.substring(!withSymbol
                        ? spannable.getSpanStart(charStyle) + 1/*skip "#" sign*/
                        : spannable.getSpanStart(charStyle), spannable.getSpanEnd(charStyle))));
        return hashTags;
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
        if (listener != null) {
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
            if (sign == '#' || sign == '@') {
                startIndexOfNextHashSign = index;
                nextNotLetterDigitCharIndex = findNextValidHashTagChar(text, startIndexOfNextHashSign);
                ((Spannable) text).setSpan(listener != null
                                ? new ClickableForegroundColorSpan(hashtagColor, listener)
                                : new ForegroundColorSpan(hashtagColor)
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

    public interface OnSocialClickListener {

        void onClick(String hashTag);
    }
}