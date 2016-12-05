package io.github.hendraanggrian.socialview;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import rx.Observable;

public final class ClickableForegroundColorSpan extends ClickableSpan {

    private final int color;
    private final OnClickListener listener;

    public ClickableForegroundColorSpan(@ColorInt int color, @NonNull OnClickListener listener) {
        this.color = color;
        this.listener = listener;
    }

    @Override
    public void updateDrawState(TextPaint textPaint) {
        textPaint.setColor(color);
    }

    @Override
    public void onClick(View widget) {
        Observable.just(widget)
                .map(view -> (TextView) view)
                .map(TextView::getText)
                .map(charSequence -> (Spanned) charSequence)
                .subscribe(spanned -> {
                    int start = spanned.getSpanStart(ClickableForegroundColorSpan.this);
                    int end = spanned.getSpanEnd(ClickableForegroundColorSpan.this);
                    listener.onClick(spanned.subSequence(start + 1/*skip "#" sign*/, end).toString());
                });
    }

    public interface OnClickListener {
        void onClick(String hashTag);
    }
}