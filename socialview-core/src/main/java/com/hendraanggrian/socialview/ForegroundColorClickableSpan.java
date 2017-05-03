package com.hendraanggrian.socialview;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

/**
 * Combination of {@link ForegroundColorSpan} and {@link ClickableSpan}.
 *
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
abstract class ForegroundColorClickableSpan extends ClickableSpan {

    abstract void onClick(@NonNull TextView v, @NonNull CharSequence text);

    @ColorInt private final int color;
    private final boolean underlined;

    ForegroundColorClickableSpan(@ColorInt int color, boolean underlined) {
        this.color = color;
        this.underlined = underlined;
    }

    @Override
    public void onClick(View widget) {
        TextView textView = (TextView) widget;
        Spanned spanned = (Spanned) textView.getText();
        onClick(textView, spanned.subSequence(
                spanned.getSpanStart(this) + 1,
                spanned.getSpanEnd(this)
        ));
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(color);
        if (underlined)
            ds.setUnderlineText(true);
    }
}