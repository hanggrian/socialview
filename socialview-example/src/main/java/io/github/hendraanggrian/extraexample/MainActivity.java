package io.github.hendraanggrian.extraexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import io.github.hendraanggrian.extra.Extras;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NextActivity.class).putExtras(new Extras()
                        .putString("EXTRA_STRING", null)
                        .putInt("EXTRA_INT", 12)
                        .putSerializable("EXTRA_SERIALIZABLE", new Model("HEY", 3))
                        .toBundle()));
            }
        });
    }
}