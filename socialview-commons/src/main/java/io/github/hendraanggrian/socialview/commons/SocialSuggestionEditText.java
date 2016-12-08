package io.github.hendraanggrian.socialview.commons;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

import io.github.hendraanggrian.socialview.SocialView;
import io.github.hendraanggrian.socialview.SocialViewBase;
import rx.Observable;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class SocialSuggestionEditText extends MultiAutoCompleteTextView implements SocialViewBase {

    private SocialView socialView;
    private ArrayAdapter<String> hashtagAdapter;
    private ArrayAdapter usernameAdapter;
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Observable.just(s)
                    .filter(charSequence -> charSequence.length() > 0)
                    .filter(charSequence -> count == 1)
                    .map(charSequence -> charSequence.charAt(start))
                    .subscribe(character -> {
                        switch (character) {
                            case '#':
                                if (getAdapter() == null || getAdapter() != hashtagAdapter)
                                    setAdapter(hashtagAdapter);
                                break;
                            case '@':
                                if (getAdapter() == null || getAdapter() != usernameAdapter)
                                    setAdapter(usernameAdapter);
                                break;
                        }
                    });
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public SocialSuggestionEditText(Context context) {
        super(context);
        init(context, null);
    }

    public SocialSuggestionEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SocialSuggestionEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SocialSuggestionEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        if (attrs == null)
            socialView = new SocialView(this, context);
        else
            socialView = new SocialView(this, context, attrs);
        hashtagAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line);
        usernameAdapter = new LabelAvatarAdapter(context);

        setTokenizer(new SocialTokenizer(isHashtagEnabled(), isUsernameEnabled()));
        setThreshold(1);
        addTextChangedListener(watcher);
    }

    @Override
    public void setHashtagColor(@ColorInt int color) {
        socialView.setHashtagColor(color);
    }

    @Override
    public void setHashtagColorRes(@ColorRes int colorRes) {
        socialView.setHashtagColorRes(colorRes);
    }

    @Override
    public void setUsernameColor(@ColorInt int color) {
        socialView.setUsernameColor(color);
    }

    @Override
    public void setUsernameColorRes(@ColorRes int colorRes) {
        socialView.setUsernameColorRes(colorRes);
    }

    @Override
    public void setHashtagEnabled(boolean enabled) {
        socialView.setHashtagEnabled(enabled);
    }

    @Override
    public void setUsernameEnabled(boolean enabled) {
        socialView.setUsernameEnabled(enabled);
    }

    @Override
    public void setOnSocialClickListener(@Nullable SocialView.OnSocialClickListener listener) {
        socialView.setOnSocialClickListener(listener);
    }

    @Override
    public int getHashtagColor() {
        return socialView.getHashtagColor();
    }

    @Override
    public int getUsernameColor() {
        return socialView.getUsernameColor();
    }

    @Override
    public boolean isHashtagEnabled() {
        return socialView.isHashtagEnabled();
    }

    @Override
    public boolean isUsernameEnabled() {
        return socialView.isUsernameEnabled();
    }

    @NonNull
    @Override
    public List<String> getHashtags() {
        return socialView.getHashtags();
    }

    @NonNull
    @Override
    public List<String> getHashtags(boolean withSymbol) {
        return socialView.getHashtags(withSymbol);
    }

    @NonNull
    @Override
    public List<String> getUsernames() {
        return socialView.getHashtags();
    }

    @NonNull
    @Override
    public List<String> getUsernames(boolean withSymbol) {
        return socialView.getUsernames();
    }

    public void setUsernameAdapter(@NonNull ArrayAdapter adapter){
        usernameAdapter = adapter;
    }

    public void addHashtagSuggestions(@NonNull String... suggestions) {
        hashtagAdapter.addAll(suggestions);
    }

    public void removeHashtagSuggestions(@NonNull String... suggestions) {
        Observable.from(suggestions).forEach(hashtagAdapter::remove);
    }

    public void clearHashtagSuggestions() {
        hashtagAdapter.clear();
    }

    private static class SocialTokenizer implements Tokenizer {
        @NonNull private final List<Character> symbols = new ArrayList<>();

        private SocialTokenizer(boolean hashtagEnabled, boolean atEnabled) {
            if (hashtagEnabled)
                symbols.add('#');
            if (atEnabled)
                symbols.add('@');
        }

        public int findTokenStart(CharSequence text, int cursor) {
            int i = cursor;
            while (i > 0 && !symbols.contains(text.charAt(i - 1)))
                i--;
            while (i < cursor && text.charAt(i) == ' ')
                i++;
            return i;
        }

        public int findTokenEnd(CharSequence text, int cursor) {
            int i = cursor;
            int len = text.length();
            while (i < len)
                if (symbols.contains(text.charAt(i)))
                    return i;
                else
                    i++;
            return len;
        }

        public CharSequence terminateToken(CharSequence text) {
            int i = text.length();
            while (i > 0 && text.charAt(i - 1) == ' ')
                i--;

            if (i > 0 && symbols.contains(text.charAt(i - 1))) {
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