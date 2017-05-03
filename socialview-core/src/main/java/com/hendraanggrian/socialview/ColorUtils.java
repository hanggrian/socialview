package com.hendraanggrian.socialview;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
final class ColorUtils {

    // Probably the only reason why socialview imports appcompat-v7 instead of lower support-v4: to get current app's theme.
    @ColorInt
    static int getThemeAccentColor(@NonNull Resources.Theme theme, @ColorInt int defaultColor) {
        TypedValue value = new TypedValue();
        return theme.resolveAttribute(R.attr.colorAccent, value, true)
                ? value.data
                : defaultColor;
    }

    @ColorInt
    static int getColor(@NonNull Context context, @ColorRes int colorRes) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                ? context.getColor(colorRes)
                : ContextCompat.getColor(context, colorRes);
    }
}