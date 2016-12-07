package io.github.hendraanggrian.socialviewexample;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import io.github.hendraanggrian.socialview.SocialView;
import io.github.hendraanggrian.socialview.commons.SocialAutoCompleteTextView;
import io.github.hendraanggrian.socialview.commons.model.Username;

public class MainActivity extends AppCompatActivity implements SocialView.OnSocialClickListener {

    private SocialAutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        autoCompleteTextView = (SocialAutoCompleteTextView) findViewById(R.id.socialautocompletetextview);
        autoCompleteTextView.setOnSocialClickListener(this);
        autoCompleteTextView.addHashtagSuggestions("Hello", "World");
        autoCompleteTextView.addUsernameSuggestions(new Username("Test", ContextCompat.getDrawable(this, R.mipmap.ic_launcher)), new Username("Hehe", ContextCompat.getDrawable(this, R.mipmap.ic_launcher)));

        autoCompleteTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                for (String hashtag : autoCompleteTextView.getHashtags())
                    Log.d("hashtag", hashtag);
                for (String at : autoCompleteTextView.getUsernames())
                    Log.d("at", at);
                return false;
            }
        });
    }

    @Override
    public void onClick(String hashTag) {
        Toast.makeText(this, hashTag, Toast.LENGTH_SHORT).show();
    }
}