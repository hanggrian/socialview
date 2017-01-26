package io.github.hendraanggrian.socialview;

import android.content.Context;
import android.content.res.TypedArray;
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
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.hendraanggrian.socialview.internal.ClickableForegroundColorSpan;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public final class SocialViewAttacher implements SocialView, TextWatcher {

    static final char HASHTAG = '#';
    static final char MENTION = '@';

    @NonNull private final TextView view;
    @ColorInt private int hashtagColor;
    @ColorInt private int mentionColor;
    private boolean hashtagEnabled;
    private boolean mentionEnabled;

    @Nullable private OnSocialClickListener hashtagListener;
    @Nullable private OnSocialClickListener mentionListener;
    @Nullable private SocialTextWatcher hashtagWatcher;
    @Nullable private SocialTextWatcher mentionWatcher;

    private boolean isHashtagEditing;
    private boolean isMentionEditing;

    SocialViewAttacher(@NonNull TextView textView, @NonNull Context context) {
        this(textView, context, null);
    }

    SocialViewAttacher(@NonNull TextView textView, @NonNull Context context, @Nullable AttributeSet attrs) {
        view = textView;
        view.setText(textView.getText(), TextView.BufferType.SPANNABLE);
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.addTextChangedListener(this);
        if (attrs != null) {
            TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SocialTextView, 0, 0);
            hashtagColor = array.getColor(R.styleable.SocialTextView_hashtagColor, getDefaultColor(context));
            mentionColor = array.getColor(R.styleable.SocialTextView_mentionColor, getDefaultColor(context));
            hashtagEnabled = array.getBoolean(R.styleable.SocialTextView_hashtagEnabled, true);
            mentionEnabled = array.getBoolean(R.styleable.SocialTextView_mentionEnabled, true);
            array.recycle();
        } else {
            hashtagColor = getDefaultColor(context);
            mentionColor = getDefaultColor(context);
            hashtagEnabled = true;
            mentionEnabled = true;
        }
        colorize();
    }

    @Override
    public void setHashtagColor(@ColorInt int color) {
        this.hashtagColor = color;
        colorize();
    }

    @Override
    public void setHashtagColorRes(@ColorRes int colorRes) {
        this.hashtagColor = ContextCompat.getColor(view.getContext(), colorRes);
        colorize();
    }

    @Override
    public void setMentionColor(@ColorInt int color) {
        this.mentionColor = color;
        colorize();
    }

    @Override
    public void setMentionColorRes(@ColorRes int colorRes) {
        this.mentionColor = ContextCompat.getColor(view.getContext(), colorRes);
        colorize();
    }

    @Override
    public void setHashtagEnabled(boolean enabled) {
        this.hashtagEnabled = enabled;
        colorize();
    }

    @Override
    public void setMentionEnabled(boolean enabled) {
        this.mentionEnabled = enabled;
        colorize();
    }

    @Override
    public void setOnHashtagClickListener(@Nullable OnSocialClickListener listener) {
        this.hashtagListener = listener;
    }

    @Override
    public void setOnMentionClickListener(@Nullable OnSocialClickListener listener) {
        this.mentionListener = listener;
    }

    @Override
    public void setHashtagTextChangedListener(@Nullable SocialTextWatcher watcher) {
        this.hashtagWatcher = watcher;
    }

    @Override
    public void setMentionTextChangedListener(@Nullable SocialTextWatcher watcher) {
        this.mentionWatcher = watcher;
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
        return extract(Pattern.compile("#(\\w+)"));
    }

    @NonNull
    @Override
    public List<String> getMentions() {
        return extract(Pattern.compile("@(\\w+)"));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d("onTextChanged", String.format("s=%s  start=%s    before=%s   count=%s", s, start, before, count));
        if (s.length() > 0) {
            final Spannable spannable = (Spannable) view.getText();
            for (CharacterStyle style : spannable.getSpans(0, s.length(), CharacterStyle.class))
                spannable.removeSpan(style);
            colorize(spannable);

            if (before > 0) {
                if (!Character.isLetterOrDigit(s.charAt(start - before))) {
                    isHashtagEditing = false;
                    isMentionEditing = false;
                } else if (hashtagWatcher != null && isHashtagEditing) {
                    hashtagWatcher.onTextChanged(view, s.subSequence(indexOfPreviousSocialChar(s, 0, start - before) + 1, start).toString());
                } else if (mentionWatcher != null && isMentionEditing) {
                    mentionWatcher.onTextChanged(view, s.subSequence(indexOfPreviousSocialChar(s, 0, start - before) + 1, start).toString());
                }
            } else if (start < s.length()) {
                switch (s.charAt(start)) {
                    case HASHTAG:
                        isHashtagEditing = true;
                        break;
                    case MENTION:
                        isMentionEditing = true;
                        break;
                    default:
                        if (!Character.isLetterOrDigit(s.charAt(start))) {
                            isHashtagEditing = false;
                            isMentionEditing = false;
                        } else if (hashtagWatcher != null && isHashtagEditing) {
                            hashtagWatcher.onTextChanged(view, s.subSequence(indexOfPreviousSocialChar(s, 0, start) + 1, start + count).toString());
                        } else if (mentionWatcher != null && isMentionEditing) {
                            mentionWatcher.onTextChanged(view, s.subSequence(indexOfPreviousSocialChar(s, 0, start) + 1, start + count).toString());
                        }
                        break;
                }
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    private int getDefaultColor(@NonNull Context context) {
        final TypedValue value = new TypedValue();
        return context.getTheme().resolveAttribute(R.attr.colorAccent, value, true)
                ? value.data
                : view.getCurrentTextColor();
    }

    private void colorize() {
        colorize(view.getText());
    }

    private void colorize(CharSequence text) {
        int index = 0;
        while (index < text.length() - 1) {
            if (text.charAt(index) == HASHTAG && hashtagEnabled)
                ((Spannable) text).setSpan(hashtagListener != null
                                ? new ClickableForegroundColorSpan(hashtagColor, hashtagListener)
                                : new ForegroundColorSpan(hashtagColor)
                        , index, indexOfNextSocialChar(text, index), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            else if (text.charAt(index) == MENTION && mentionEnabled)
                ((Spannable) text).setSpan(mentionListener != null
                                ? new ClickableForegroundColorSpan(mentionColor, mentionListener)
                                : new ForegroundColorSpan(mentionColor)
                        , index, indexOfNextSocialChar(text, index), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            index++;
        }
    }

    private int indexOfNextSocialChar(CharSequence text, int start) {
        for (int i = start + 1; i < text.length(); i++)
            if (!Character.isLetterOrDigit(text.charAt(i)))
                return i;
        return text.length();
    }

    private int indexOfPreviousSocialChar(CharSequence text, int start, int end) {
        for (int i = end; i > start; i--)
            if (!Character.isLetterOrDigit(text.charAt(i)))
                return i;
        return start;
    }

    @NonNull
    private List<String> extract(Pattern pattern) {
        final List<String> list = new ArrayList<>();
        final Matcher matcher = pattern.matcher(view.getText().toString());
        while (matcher.find())
            list.add(matcher.group(1));
        return list;
    }

    @NonNull
    public static SocialViewAttacher attach(@NonNull TextView view) {
        return new SocialViewAttacher(view, view.getContext());
    }
}