package com.hendraanggrian.appcompat.socialview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.hendraanggrian.appcompat.socialview.test.R;

public class TestActivity extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    setTheme(R.style.Theme_MaterialComponents_Light_NoActionBar_Bridge);
  }
}
