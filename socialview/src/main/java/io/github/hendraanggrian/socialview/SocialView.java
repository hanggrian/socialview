package io.github.hendraanggrian.socialview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import rx.Observable;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public final class SocialView {

    private int colorHashtag;
    private int colorAt;
    private Set<Character> additionalHashtag = new HashSet<>();
    private Set<Character> additionalAt = new HashSet<>();

    private OnSocialClickListener listener;

    SocialView(@NonNull TextView view, @NonNull Context context) {
        getDefaultColor(context)
                .subscribe(colorAccent -> {
                    colorHashtag = colorAccent;
                    colorAt = colorAccent;
                }, throwable -> {
                }, () -> refresh(view));
    }

    SocialView(@NonNull TextView view, @NonNull Context context, @NonNull AttributeSet attrs) {
        Observable.just(context.getTheme().obtainStyledAttributes(attrs, R.styleable.SocialTextView, 0, 0))
                .subscribe(array -> {
                    getDefaultColor(context)
                            .subscribe(colorAccent -> {
                                colorHashtag = array.getColor(R.styleable.SocialTextView_color_hashtag, colorAccent);
                                colorAt = array.getColor(R.styleable.SocialTextView_color_at, colorAccent);
                            });

                    Observable.just(array.getString(R.styleable.SocialTextView_color_hashtag))
                            .filter(s -> s != null)
                            .map(String::toCharArray)
                            .subscribe(chars ->
                                    Observable.range(0, chars.length).map(i ->
                                            chars[i]).forEach(character -> additionalHashtag.add(character)));

                    Observable.just(array.getString(R.styleable.SocialTextView_color_at))
                            .filter(s -> s != null)
                            .map(String::toCharArray)
                            .subscribe(chars ->
                                    Observable.range(0, chars.length).map(i ->
                                            chars[i]).forEach(character -> additionalAt.add(character)));

                    array.recycle();
                }, throwable -> {
                }, () -> refresh(view));
    }

    public static SocialView attach(@NonNull TextView textView) {
        return new SocialView(textView, textView.getContext());
    }

    void setOnClickListener(TextView view, OnSocialClickListener listener) {
        this.listener = listener;
        refresh(view);
    }

    private void refresh(TextView textView) {
        textView.setText(textView.getText(), TextView.BufferType.SPANNABLE);
        if (listener != null) {
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setHighlightColor(Color.TRANSPARENT);
        }
        setColorsToAllHashTags(textView.getText());
    }

    private Observable<Integer> getDefaultColor(@NonNull Context context) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, value, true);
        return Observable.just(value.data);
    }

    void eraseAndColorizeAllText(TextView textView, CharSequence text) {
        Observable.just(textView)
                .map(TextView::getText)
                .map(charSequence -> (Spannable) charSequence)
                .subscribe(
                        spannable -> Observable.from(spannable.getSpans(0, text.length(), CharacterStyle.class)).forEach(spannable::removeSpan),
                        throwable -> {
                        }, () -> setColorsToAllHashTags(text));
    }

    private void setColorsToAllHashTags(CharSequence text) {
        int startIndexOfNextHashSign;

        int index = 0;
        while (index < text.length() - 1) {
            char sign = text.charAt(index);
            int nextNotLetterDigitCharIndex = index + 1; // we assume it is next. if if was not changed by findNextValidHashTagChar then index will be incremented by 1
            if (sign == '#') {
                startIndexOfNextHashSign = index;
                nextNotLetterDigitCharIndex = findNextValidHashTagChar(text, startIndexOfNextHashSign);
                setColorForHashTagToTheEnd(text, startIndexOfNextHashSign, nextNotLetterDigitCharIndex);
            }

            index = nextNotLetterDigitCharIndex;
        }
    }

    private int findNextValidHashTagChar(CharSequence text, int start) {
        int nonLetterDigitCharIndex = -1; // skip first sign '#"
        for (int index = start + 1; index < text.length(); index++) {

            char sign = text.charAt(index);

            boolean isValidSign = Character.isLetterOrDigit(sign) || additionalHashtag.contains(sign);
            if (!isValidSign) {
                nonLetterDigitCharIndex = index;
                break;
            }
        }
        if (nonLetterDigitCharIndex == -1) {
            // we didn't find non-letter. We are at the end of text
            nonLetterDigitCharIndex = text.length();
        }

        return nonLetterDigitCharIndex;
    }

    private void setColorForHashTagToTheEnd(@NonNull CharSequence text, int startIndex, int nextNotLetterDigitCharIndex) {
        Spannable s = (Spannable) text;

        CharacterStyle span;

        if (listener != null) {
            span = new ClickableForegroundColorSpan(colorHashtag, listener);
        } else {
            // no need for clickable span because it is messing with selection when click
            span = new ForegroundColorSpan(colorHashtag);
        }

        s.setSpan(span, startIndex, nextNotLetterDigitCharIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public List<String> getAllHashTags(@NonNull CharSequence text, boolean withHashes) {
        String original = text.toString();
        Spannable spannable = (Spannable) text;

        // use set to exclude duplicates
        Set<String> hashTags = new LinkedHashSet<>();

        for (CharacterStyle span : spannable.getSpans(0, original.length(), CharacterStyle.class)) {
            hashTags.add(
                    original.substring(!withHashes ? spannable.getSpanStart(span) + 1/*skip "#" sign*/
                                    : spannable.getSpanStart(span),
                            spannable.getSpanEnd(span)));
        }

        return new ArrayList<>(hashTags);
    }

    public List<String> getAllHashTags(@NonNull CharSequence text) {
        return getAllHashTags(text, false);
    }

    public interface OnSocialClickListener {
        void onClick(String hashTag);
    }
}