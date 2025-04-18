package com.hanggrian.socialview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.Patterns;
import android.util.TypedValue;
import android.widget.TextView;
import androidx.core.util.PatternsCompat;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SocialViewTester<T extends TextView & SocialView> {
    private final T view;

    public SocialViewTester(T view) {
        this.view = view;
    }

    public void hashtagPattern() {
        assertEquals("#(\\w+)", view.getHashtagPattern().pattern());
        view.setHashtagPattern(Patterns.PHONE);
        assertEquals(Patterns.PHONE, view.getHashtagPattern());
    }

    public void mentionPattern() {
        assertEquals("@(\\w+)", view.getMentionPattern().pattern());
        view.setMentionPattern(Patterns.PHONE);
        assertEquals(Patterns.PHONE, view.getMentionPattern());
    }

    public void hyperlinkPattern() {
        assertEquals(PatternsCompat.WEB_URL, view.getHyperlinkPattern());
        view.setHyperlinkPattern(Patterns.PHONE);
        assertEquals(Patterns.PHONE, view.getHyperlinkPattern());
    }

    public void hashtagEnabled() {
        assertTrue(view.isHashtagEnabled());
        view.setHashtagEnabled(false);
        assertFalse(view.isHashtagEnabled());
    }

    public void mentionEnabled() {
        assertTrue(view.isMentionEnabled());
        view.setMentionEnabled(false);
        assertFalse(view.isMentionEnabled());
    }

    public void hyperlinkEnabled() {
        assertTrue(view.isHyperlinkEnabled());
        view.setHyperlinkEnabled(false);
        assertFalse(view.isHyperlinkEnabled());
    }

    public void hashtagColor() {
        assertEquals(getThemeAccentColor(view.getContext()), view.getHashtagColor());
        view.setHashtagColor(Color.RED);
        assertEquals(Color.RED, view.getHashtagColor());
        view.setHashtagColors(ColorStateList.valueOf(Color.GREEN));
        assertEquals(Color.GREEN, view.getHashtagColor());
    }

    public void mentionColor() {
        assertEquals(getThemeAccentColor(view.getContext()), view.getMentionColor());
        view.setMentionColor(Color.RED);
        assertEquals(Color.RED, view.getMentionColor());
        view.setMentionColors(ColorStateList.valueOf(Color.GREEN));
        assertEquals(Color.GREEN, view.getMentionColor());
    }

    public void hyperlinkColor() {
        assertEquals(getThemeAccentColor(view.getContext()), view.getHyperlinkColor());
        view.setHyperlinkColor(Color.RED);
        assertEquals(Color.RED, view.getHyperlinkColor());
        view.setHyperlinkColors(ColorStateList.valueOf(Color.GREEN));
        assertEquals(Color.GREEN, view.getHyperlinkColor());
    }

    public void getHashtags() {
        view.setText("Here's a #cool #hashtag");
        assertThat(view.getHashtags()).containsExactly("cool", "hashtag");
    }

    public void getMentions() {
        view.setText("Here's a #cool #hashtag");
        assertThat(view.getHashtags()).containsExactly("cool", "hashtag");
    }

    private static int getThemeAccentColor(Context context) {
        int colorAttr;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            colorAttr = android.R.attr.colorAccent;
        } else {
            // get colorAccent defined for AppCompat
            colorAttr =
                context.getResources().getIdentifier(
                    "colorAccent",
                    "attr",
                    context.getPackageName()
                );
        }
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(colorAttr, outValue, true);
        return outValue.data;
    }
}
