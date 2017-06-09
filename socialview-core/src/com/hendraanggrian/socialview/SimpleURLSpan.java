package com.hendraanggrian.socialview;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.URLSpan;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
@SuppressLint("ParcelCreator")
class SimpleURLSpan extends URLSpan {

    @NonNull private final Spannable spannable;
    @ColorInt private final int color;

    SimpleURLSpan(@NonNull Spannable spannable, @ColorInt int color) {
        super("");
        this.spannable = spannable;
        this.color = color;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getURL());
    }

    @Override
    public String getURL() {
        return spannable.subSequence(spannable.getSpanStart(this), spannable.getSpanEnd(this)).toString();
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.linkColor = color;
        super.updateDrawState(ds);
    }
}