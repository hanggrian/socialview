package com.hanggrian.appcompat.socialview;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.hanggrian.appcompat.socialview.test.R;
import com.hanggrian.appcompat.socialview.widget.SocialEditText;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.internal.DoNotInstrument;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
@DoNotInstrument
public class CustomStyleTest {
    private AppCompatActivity activity;
    private SocialEditText view;

    @Before
    public void setup() {
        activity = Robolectric.buildActivity(StyleTestActivity.class).setup().get();
        view =
            (SocialEditText) activity.getLayoutInflater().inflate(
                R.layout.test_socialedittext,
                null
            );
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
            setTheme(R.style.MyAppTheme);
        }
    }
}
