package com.hendraanggrian.appcompat.socialview;

import static org.junit.Assert.assertEquals;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.hendraanggrian.appcompat.widget.SocialEditText;
import com.hendraanggrian.appcompat.socialview.test.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.internal.DoNotInstrument;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
@DoNotInstrument
public class CustomStyleTest {

    private AppCompatActivity activity;
    private SocialEditText view;

    @Before
    public void setup() {
        activity = Robolectric.buildActivity(StyleTestActivity.class).setup().get();
        view = (SocialEditText) activity.getLayoutInflater().inflate(R.layout.test_socialedittext, null);
    }

    @Test
    public void test() {
        assertEquals(Color.RED, view.getHashtagColor());
        assertEquals(Color.GREEN, view.getMentionColor());
        assertEquals(Color.BLUE, view.getHyperlinkColor());
    }

    private static class StyleTestActivity extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            setTheme(com.hendraanggrian.appcompat.socialview.test.R.style.MyAppTheme);
        }
    }
}
