package io.github.hendraanggrian.socialautocompletetextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import rx.Observable;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class SocialAutoCompleteTextView extends MultiAutoCompleteTextView implements TextWatcher {

    private int hashtagColor;
    private int atColor;
    private boolean hashtagEnabled;
    private boolean atEnabled;

    private ArrayAdapter<String> hashtagAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line);
    private ArrayAdapter<String> atAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line);
    private SocialAutoCompleteTextView.OnSocialClickListener listener;

    public SocialAutoCompleteTextView(Context context) {
        super(context);
        init(context);
    }

    public SocialAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SocialAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SocialAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(@NonNull Context context) {
        getDefaultColor(context).subscribe(colorAccent -> {
            hashtagColor = colorAccent;
            atColor = colorAccent;
            hashtagEnabled = true;
            atEnabled = true;
        }, throwable -> {
        }, () -> {
            setTokenizer(new SocialTokenizer(hashtagEnabled, atEnabled));
            refresh();
        });
    }

    private void init(@NonNull Context context, @NonNull AttributeSet attrs) {
        getDefaultColor(context).subscribe(colorAccent -> {
            TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SocialTextView, 0, 0);
            hashtagColor = array.getColor(R.styleable.SocialTextView_hashtagColor, colorAccent);
            atColor = array.getColor(R.styleable.SocialTextView_atColor, colorAccent);
            hashtagEnabled = array.getBoolean(R.styleable.SocialTextView_hashtagColor, true);
            atEnabled = array.getBoolean(R.styleable.SocialTextView_atEnabled, true);
            array.recycle();
        }, throwable -> {
        }, () -> {
            setTokenizer(new SocialTokenizer(hashtagEnabled, atEnabled));
            refresh();
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
            if (count == 1 && s.charAt(start) == '#')
                setAdapter(hashtagAdapter);
            else if (count == 1 && s.charAt(start) == '@')
                setAdapter(atAdapter);

            Observable.just(this)
                    .map(TextView::getText)
                    .map(text -> (Spannable) text)
                    .subscribe(
                            spannable -> Observable.from(spannable.getSpans(0, s.length(), CharacterStyle.class)).forEach(spannable::removeSpan),
                            throwable -> {
                            }, () -> setColorsToAllHashTags(s));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    public void setHashtagEnabled(boolean hashtagEnabled) {
        this.hashtagEnabled = hashtagEnabled;
        setTokenizer(new SocialTokenizer(hashtagEnabled, atEnabled));
    }

    public void setAtEnabled(boolean atEnabled) {
        this.atEnabled = atEnabled;
        setTokenizer(new SocialTokenizer(hashtagEnabled, atEnabled));
    }

    public void addHashtagSuggestions(@NonNull String... hashtags) {
        hashtagAdapter.addAll(hashtags);
    }

    public void removeHashtagSuggestions(@NonNull String... hashtags) {
        Observable.from(hashtags).forEach(s -> hashtagAdapter.remove(s));
    }

    public void clearHashtagSuggestions() {
        hashtagAdapter.clear();
    }

    public void addAtSuggestions(@NonNull String... ats) {
        atAdapter.addAll(ats);
    }

    public void removeAtSuggestions(@NonNull String... ats) {
        Observable.from(ats).forEach(s -> atAdapter.remove(s));
    }

    public void clearAtSuggestions() {
        atAdapter.clear();
    }

    public void setOnSocialClickListener(OnSocialClickListener listener) {
        this.listener = listener;
        refresh();
    }

    private void refresh() {
        setText(getText(), TextView.BufferType.SPANNABLE);
        if (listener != null) {
            setMovementMethod(LinkMovementMethod.getInstance());
            setHighlightColor(Color.TRANSPARENT);
        }
        setColorsToAllHashTags(getText());
    }

    private Observable<Integer> getDefaultColor(@NonNull Context context) {
        final TypedValue value = new TypedValue();
        return context.getTheme().resolveAttribute(R.attr.colorAccent, value, true)
                ? Observable.just(value.data)
                : Observable.just(getCurrentTextColor());
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

            boolean isValidSign = Character.isLetterOrDigit(sign);
            if (!isValidSign) {
                nonLetterDigitCharIndex = index;
                break;
            }
        }
        if (nonLetterDigitCharIndex == -1)
            // we didn't find non-letter. We are at the end of text
            nonLetterDigitCharIndex = text.length();
        return nonLetterDigitCharIndex;
    }

    private void setColorForHashTagToTheEnd(@NonNull CharSequence text, int startIndex, int nextNotLetterDigitCharIndex) {
        ((Spannable) text).setSpan(listener != null
                        ? new ClickableForegroundColorSpan(hashtagColor, listener)
                        : new ForegroundColorSpan(hashtagColor)
                , startIndex, nextNotLetterDigitCharIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public List<String> getAllHashTags(@NonNull CharSequence text, boolean withHashes) {
        String original = text.toString();
        Spannable spannable = (Spannable) text;
        // use set to exclude duplicates
        Set<String> hashTags = new LinkedHashSet<>();
        for (CharacterStyle span : spannable.getSpans(0, original.length(), CharacterStyle.class))
            hashTags.add(original.substring(!withHashes
                            ? spannable.getSpanStart(span) + 1/*skip "#" sign*/
                            : spannable.getSpanStart(span),
                    spannable.getSpanEnd(span)));
        return new ArrayList<>(hashTags);
    }

    public List<String> getAllHashTags(@NonNull CharSequence text) {
        return getAllHashTags(text, false);
    }

    public interface OnSocialClickListener {

        void onClick(String hashTag);
    }

    public static class SocialTokenizer implements Tokenizer {
        private final boolean hashtagEnabled, atEnabled;

        public SocialTokenizer(boolean hashtagEnabled, boolean atEnabled) {
            this.hashtagEnabled = hashtagEnabled;
            this.atEnabled = atEnabled;
        }

        public int findTokenStart(CharSequence text, int cursor) {
            int i = cursor;
            while (i > 0)
                if (hashtagEnabled && text.charAt(i - 1) != '#')
                    i--;
                else if (atEnabled && text.charAt(i - 1) != '@')
                    i--;
            while (i < cursor && text.charAt(i) == ' ')
                i++;
            return i;
        }

        public int findTokenEnd(CharSequence text, int cursor) {
            int i = cursor;
            int len = text.length();
            while (i < len)
                if (hashtagEnabled && text.charAt(i) == '#')
                    return i;
                else if (atEnabled && text.charAt(i) == '@')
                    return i;
                else
                    i++;
            return len;
        }

        public CharSequence terminateToken(CharSequence text) {
            int i = text.length();
            while (i > 0 && text.charAt(i - 1) == ' ')
                i--;

            if (hashtagEnabled && i > 0 && text.charAt(i - 1) == '#') {
                return text;
            } else if (atEnabled && i > 0 && text.charAt(i - 1) == '@') {
                return text;
            } else if (text instanceof Spanned) {
                SpannableString sp = new SpannableString(text + " ");
                TextUtils.copySpansFrom((Spanned) text, 0, text.length(), Object.class, sp, 0);
                return sp;
            } else {
                return text + " ";
            }
        }
    }
}