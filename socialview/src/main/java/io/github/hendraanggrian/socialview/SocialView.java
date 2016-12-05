package io.github.hendraanggrian.socialview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.TextUtils;
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
final class SocialView {

    private int colorHashtag;
    private int colorAt;
    private Set<Character> additionalHashtag = new HashSet<>();
    private Set<Character> additionalAt = new HashSet<>();

    public SocialView(@ColorInt int colorHashtag, @ColorInt int colorAt) {
        this.colorHashtag = colorHashtag;
        this.colorAt = colorAt;
    }

    public SocialView(@NonNull Context context, @NonNull AttributeSet attrs) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, value, true);

        final TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SocialTextView, 0, 0);
        colorHashtag = array.getColor(R.styleable.SocialTextView_color_hashtag, value.data);
        colorAt = array.getColor(R.styleable.SocialTextView_color_at, value.data);

        final String hashtags = array.getString(R.styleable.SocialTextView_color_hashtag);
        if (!TextUtils.isEmpty(hashtags))
            for (char hashtag : hashtags.toCharArray())
                additionalHashtag.add(hashtag);

        final String ats = array.getString(R.styleable.SocialTextView_color_at);
        if (!TextUtils.isEmpty(ats))
            for (char at : ats.toCharArray())
                additionalAt.add(at);

        array.recycle();
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

    void setColorsToAllHashTags(CharSequence text) {
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

    int findNextValidHashTagChar(CharSequence text, int start) {
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

    void setColorForHashTagToTheEnd(@NonNull CharSequence text, int startIndex, int nextNotLetterDigitCharIndex) {
        Spannable s = (Spannable) text;

        CharacterStyle span;

        if (mOnHashTagClickListener != null) {
            span = new ClickableForegroundColorSpan(mHashTagWordColor, this);
        } else {
            // no need for clickable span because it is messing with selection when click
            span = new ForegroundColorSpan(mHashTagWordColor);
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
}