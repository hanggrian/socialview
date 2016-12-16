package io.github.hendraanggrian.socialview;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

final class ClickableForegroundColorSpan extends ClickableSpan {

    @ColorInt private final int color;
    @NonNull private final SocialView.OnSocialClickListener listener;

    ClickableForegroundColorSpan(@ColorInt int color, @NonNull SocialView.OnSocialClickListener listener) {
        this.color = color;
        this.listener = listener;
    }

    @Override
    public void updateDrawState(TextPaint textPaint) {
        textPaint.setColor(color);
    }

    @Override
    public void onClick(View widget) {
        final Spanned spanned = (Spanned) ((TextView) widget).getText();
        final int start = spanned.getSpanStart(ClickableForegroundColorSpan.this) + 1;
        final int end = spanned.getSpanEnd(ClickableForegroundColorSpan.this);
        listener.onClick(widget, spanned.subSequence(start, end).toString());
    }
}