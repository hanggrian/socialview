package com.hanggrian.appcompat.socialview;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.hanggrian.appcompat.socialview.test.R;

public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
    }
}
