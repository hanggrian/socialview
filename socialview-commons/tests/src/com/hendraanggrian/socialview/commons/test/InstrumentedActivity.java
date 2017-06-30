package com.hendraanggrian.socialview.commons.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;

import com.hendraanggrian.widget.SocialAutoCompleteTextView;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class InstrumentedActivity extends AppCompatActivity {

    public ProgressBar progressBar;
    public SocialAutoCompleteTextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrumented);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textView = (SocialAutoCompleteTextView) findViewById(R.id.textView);
    }
}