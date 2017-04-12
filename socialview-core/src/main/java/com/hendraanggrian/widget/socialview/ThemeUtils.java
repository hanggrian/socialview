package com.hendraanggrian.widget.socialview;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.TypedValue;

/**
 * Probably the only reason why socialview imports appcompat-v7 instead of lower support-v4:
 * to get current app's theme.
 *
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public final class ThemeUtils {

    @ColorInt
    public static int getColor(@NonNull Context context, @ColorInt int defaultColor) {
        return getColor(context.getTheme(), defaultColor);
    }

    @ColorInt
    public static int getColor(@NonNull Resources.Theme theme, @ColorInt int defaultColor) {
        TypedValue value = new TypedValue();
        return theme.resolveAttribute(R.attr.colorAccent, value, true)
                ? value.data
                : defaultColor;
    }
}