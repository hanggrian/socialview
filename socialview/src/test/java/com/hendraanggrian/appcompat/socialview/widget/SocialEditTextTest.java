package com.hendraanggrian.appcompat.socialview.widget;

import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import com.hendraanggrian.appcompat.socialview.SocialViewTester;
import com.hendraanggrian.appcompat.socialview.TestActivity;
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
public class SocialEditTextTest {
  private AppCompatActivity activity;
  private SocialViewTester<SocialEditText> helper;

  @Before
  public void setup() {
    activity = Robolectric.buildActivity(TestActivity.class).setup().get();
    helper = new SocialViewTester<>(new SocialEditText(activity));
  }

  @Test
  public void hashtagPattern() {
    helper.hashtagPattern();
  }

  @Test
  public void mentionPattern() {
    helper.mentionPattern();
  }

  @Test
  public void hyperlinkPattern() {
    helper.hyperlinkPattern();
  }

  @Test
  public void hashtagEnabled() {
    helper.hashtagEnabled();
  }

  @Test
  public void mentionEnabled() {
    helper.mentionEnabled();
  }

  @Test
  public void hyperlinkEnabled() {
    helper.hyperlinkEnabled();
  }

  @Test
  public void hashtagColor() {
    helper.hashtagColor();
  }

  @Test
  public void mentionColor() {
    helper.mentionColor();
  }

  @Test
  public void hyperlinkColor() {
    helper.hyperlinkColor();
  }

  @Test
  public void getHashtags() {
    helper.getHashtags();
  }

  @Test
  public void getMentions() {
    helper.getMentions();
  }
}
