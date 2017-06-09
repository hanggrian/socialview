package com.hendraanggrian.socialview;

import android.support.annotation.ColorInt;
import android.text.TextPaint;
import android.text.style.ClickableSpan;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
abstract class ForegroundColorClickableSpan extends ClickableSpan {

    @ColorInt private final int color;

    ForegroundColorClickableSpan(@ColorInt int color) {
        this.color = color;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(color);
    }
}