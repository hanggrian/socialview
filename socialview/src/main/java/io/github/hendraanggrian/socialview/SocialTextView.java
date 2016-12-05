package io.github.hendraanggrian.socialview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class SocialTextView extends TextView implements TextWatcher {

    private SocialView socialView;

    public SocialTextView(Context context, @ColorInt int colorHashtag, @ColorInt int colorAt) {
        super(context);
        this.socialView = new SocialView(colorHashtag, colorAt);
    }

    public SocialTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.socialView = new SocialView(context, attrs);
    }

    public SocialTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.socialView = new SocialView(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SocialTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.socialView = new SocialView(context, attrs);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {

    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}