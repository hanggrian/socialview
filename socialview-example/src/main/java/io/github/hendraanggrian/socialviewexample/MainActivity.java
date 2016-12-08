package io.github.hendraanggrian.socialviewexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import io.github.hendraanggrian.socialview.SocialView;
import io.github.hendraanggrian.socialview.commons.LabelAvatarAdapter;
import io.github.hendraanggrian.socialview.commons.SocialSuggestionEditText;

public class MainActivity extends AppCompatActivity implements SocialView.OnSocialClickListener {

    private SocialSuggestionEditText autoCompleteTextView;
    private LabelAvatarAdapter usernameAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameAdapter = new LabelAvatarAdapter(this);

        autoCompleteTextView = (SocialSuggestionEditText) findViewById(R.id.socialautocompletetextview);
        autoCompleteTextView.setOnSocialClickListener(this);
        autoCompleteTextView.setUsernameAdapter(usernameAdapter);

        autoCompleteTextView.addHashtagSuggestions("Hello", "World");
        //autoCompleteTextView.getUsernameAdapter().add("Test", R.mipmap.ic_launcher);
        //autoCompleteTextView.getUsernameAdapter().add("Hehe", R.mipmap.ic_launcher);

        usernameAdapter.add("Aaa");
        usernameAdapter.add("Ddd");

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