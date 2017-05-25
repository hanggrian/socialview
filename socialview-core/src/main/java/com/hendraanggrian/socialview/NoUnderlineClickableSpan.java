package com.hendraanggrian.socialview;

import android.text.TextPaint;
import android.text.style.ClickableSpan;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
abstract class NoUnderlineClickableSpan extends ClickableSpan {

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(ds.linkColor);
    }
}