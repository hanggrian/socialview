package io.github.hendraanggrian.socialviewexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import io.github.hendraanggrian.socialview.SocialEditText;
import io.github.hendraanggrian.socialview.SocialView;

public class MainActivity extends AppCompatActivity implements SocialView.OnSocialClickListener {

    private SocialEditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (SocialEditText) findViewById(R.id.socialedittext);
        editText.setOnSocialClickListener(this);
    }

    @Override
    public void onClick(String hashTag) {
        Toast.makeText(this, hashTag, Toast.LENGTH_SHORT).show();
    }
}