package io.github.hendraanggrian.socialview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class SocialEditText extends EditText {

    public SocialEditText(Context context) {
        super(context);
    }

    public SocialEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SocialEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SocialEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}