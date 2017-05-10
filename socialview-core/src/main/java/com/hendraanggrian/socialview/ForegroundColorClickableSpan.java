package com.hendraanggrian.socialview;

import android.support.annotation.ColorInt;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;

/**
 * Combination of {@link ForegroundColorSpan} and {@link ClickableSpan}.
 *
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
abstract class ForegroundColorClickableSpan extends ClickableSpan {

    @ColorInt private final int color;
    private final boolean underlined;

    ForegroundColorClickableSpan(@ColorInt int color, boolean underlined) {
        this.color = color;
        this.underlined = underlined;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(color);
        if (underlined)
            ds.setUnderlineText(true);
    }
}