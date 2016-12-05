package io.github.hendraanggrian.socialview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class SocialEditText extends EditText implements TextWatcher {

    private SocialView socialView;

    public SocialEditText(Context context) {
        super(context);
        this.socialView = new SocialView(this, context);
    }

    public SocialEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.socialView = new SocialView(this, context, attrs);
    }

    public SocialEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.socialView = new SocialView(this, context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SocialEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.socialView = new SocialView(this, context, attrs);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0)
            socialView.eraseAndColorizeAllText(this, s);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    public void setOnSocialClickListener(@NonNull SocialView.OnSocialClickListener listener) {
        socialView.setOnClickListener(this, listener);
    }
}