package io.github.hendraanggrian.socialautocompletetextviewexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import io.github.hendraanggrian.socialautocompletetextview.SocialAutoCompleteTextView;

public class MainActivity extends AppCompatActivity implements SocialAutoCompleteTextView.OnSocialClickListener {

    private SocialAutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        autoCompleteTextView = (SocialAutoCompleteTextView) findViewById(R.id.socialautocompletetextview);
        autoCompleteTextView.setOnSocialClickListener(this);
        autoCompleteTextView.addHashtagSuggestions("Hello", "World");
        autoCompleteTextView.addAtSuggestions("Test", "Lolol");
    }

    @Override
    public void onClick(String hashTag) {
        Toast.makeText(this, hashTag, Toast.LENGTH_SHORT).show();
    }
}