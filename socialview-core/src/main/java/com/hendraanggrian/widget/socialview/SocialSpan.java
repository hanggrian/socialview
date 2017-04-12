package com.hendraanggrian.widget.socialview;

import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
final class SocialSpan extends ClickableSpan {

    @ColorInt private final int color;
    @Nullable private final SociableView.OnSocialClickListener listener;

    SocialSpan(@ColorInt int color, @Nullable SociableView.OnSocialClickListener listener) {
        this.color = color;
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            TextView textView = (TextView) view;
            Spanned spanned = (Spanned) textView.getText();
            listener.onClick(textView, spanned.subSequence(
                    spanned.getSpanStart(SocialSpan.this) + 1,
                    spanned.getSpanEnd(SocialSpan.this)
            ));
        }
    }

    @Override
    public void updateDrawState(TextPaint textPaint) {
        textPaint.setColor(color);
    }
}