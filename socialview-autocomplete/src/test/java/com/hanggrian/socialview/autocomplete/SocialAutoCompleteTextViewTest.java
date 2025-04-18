package com.hanggrian.socialview.autocomplete;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.Patterns;
import android.util.TypedValue;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.PatternsCompat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.internal.DoNotInstrument;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
@DoNotInstrument
public class SocialAutoCompleteTextViewTest {
    private AppCompatActivity activity;
    private SocialAutoCompleteTextView view;

    @Before
    public void setup() {
        activity = Robolectric.buildActivity(TestActivity.class).setup().get();
        view = new SocialAutoCompleteTextView(activity);
    }

    @Test
    public void hashtagPattern() {
        assertEquals("#(\\w+)", view.getHashtagPattern().pattern());
        view.setHashtagPattern(Patterns.PHONE);
        assertEquals(Patterns.PHONE, view.getHashtagPattern());
    }

    @Test
    public void mentionPattern() {
        assertEquals("@(\\w+)", view.getMentionPattern().pattern());
        view.setMentionPattern(Patterns.PHONE);
        assertEquals(Patterns.PHONE, view.getMentionPattern());
    }

    @Test
    public void hyperlinkPattern() {
        assertEquals(PatternsCompat.WEB_URL, view.getHyperlinkPattern());
        view.setHyperlinkPattern(Patterns.PHONE);
        assertEquals(Patterns.PHONE, view.getHyperlinkPattern());
    }

    @Test
    public void hashtagEnabled() {
        assertTrue(view.isHashtagEnabled());
        view.setHashtagEnabled(false);
        assertFalse(view.isHashtagEnabled());
    }

    @Test
    public void mentionEnabled() {
        assertTrue(view.isMentionEnabled());
        view.setMentionEnabled(false);
        assertFalse(view.isMentionEnabled());
    }

    @Test
    public void hyperlinkEnabled() {
        assertTrue(view.isHyperlinkEnabled());
        view.setHyperlinkEnabled(false);
        assertFalse(view.isHyperlinkEnabled());
    }

    @Test
    public void hashtagColor() {
        assertEquals(getThemeAccentColor(view.getContext()), view.getHashtagColor());
        view.setHashtagColor(Color.RED);
        assertEquals(Color.RED, view.getHashtagColor());
        view.setHashtagColors(ColorStateList.valueOf(Color.GREEN));
        assertEquals(Color.GREEN, view.getHashtagColor());
    }

    @Test
    public void mentionColor() {
        assertEquals(getThemeAccentColor(view.getContext()), view.getMentionColor());
        view.setMentionColor(Color.RED);
        assertEquals(Color.RED, view.getMentionColor());
        view.setMentionColors(ColorStateList.valueOf(Color.GREEN));
        assertEquals(Color.GREEN, view.getMentionColor());
    }

    @Test
    public void hyperlinkColor() {
        assertEquals(getThemeAccentColor(view.getContext()), view.getHyperlinkColor());
        view.setHyperlinkColor(Color.RED);
        assertEquals(Color.RED, view.getHyperlinkColor());
        view.setHyperlinkColors(ColorStateList.valueOf(Color.GREEN));
        assertEquals(Color.GREEN, view.getHyperlinkColor());
    }

    @Test
    public void getHashtags() {
        view.setText("Here's a #cool #hashtag");
        assertThat(view.getHashtags()).containsExactly("cool", "hashtag");
    }

    @Test
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
