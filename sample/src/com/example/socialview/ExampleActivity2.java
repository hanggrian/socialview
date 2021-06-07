package com.example.socialview;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.hendraanggrian.appcompat.widget.Hashtag;
import com.hendraanggrian.appcompat.widget.HashtagArrayAdapter;
import com.hendraanggrian.appcompat.widget.Mention;
import com.hendraanggrian.appcompat.widget.MentionArrayAdapter;
import com.hendraanggrian.appcompat.widget.SocialArrayAdapter;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.hendraanggrian.appcompat.widget.SocialView;

public class ExampleActivity2 extends AppCompatActivity {

    private static final String HASHTAG1 = "follow";
    private static final String HASHTAG2 = "followme";
    private static final String HASHTAG3 = "followmeorillkillyou";
    private static final int HASHTAG2_COUNT = 1000;
    private static final int HASHTAG3_COUNT = 500;
    private static final String MENTION1_USERNAME = "dirtyhobo";
    private static final String MENTION2_USERNAME = "hobo";
    private static final String MENTION3_USERNAME = "hendraanggrian";
    private static final String MENTION2_DISPLAYNAME = "Regular Hobo";
    private static final String MENTION3_DISPLAYNAME = "Hendra Anggrian";

    private Toolbar toolbar;
    private SocialAutoCompleteTextView textView;

    private ArrayAdapter<Hashtag> defaultHashtagAdapter;
    private ArrayAdapter<Mention> defaultMentionAdapter;
    private ArrayAdapter<Person> customHashtagAdapter;
    private ArrayAdapter<Person> customMentionAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        toolbar = findViewById(R.id.toolbar);
        textView = findViewById(R.id.textView);
        setSupportActionBar(toolbar);

        defaultHashtagAdapter = new HashtagArrayAdapter<>(this);
        defaultHashtagAdapter.addAll(
            new Hashtag(HASHTAG1),
            new Hashtag(HASHTAG2, HASHTAG2_COUNT),
            new Hashtag(HASHTAG3, HASHTAG3_COUNT));

        defaultMentionAdapter = new MentionArrayAdapter<>(this);
        defaultMentionAdapter.addAll(
            new Mention(MENTION1_USERNAME),
            new Mention(MENTION2_USERNAME, MENTION2_DISPLAYNAME, android.R.drawable.sym_action_email),
            new Mention(
                MENTION3_USERNAME,
                MENTION3_DISPLAYNAME,
                "https://avatars1.githubusercontent.com/u/11507430?s=460&v=4"
            ));

        customHashtagAdapter = new PersonAdapter(this);
        customHashtagAdapter.addAll(
            new Person(HASHTAG1),
            new Person(HASHTAG2),
            new Person(HASHTAG3));

        customMentionAdapter = new PersonAdapter(this);
        customMentionAdapter.addAll(
            new Person(MENTION1_USERNAME),
            new Person(MENTION2_USERNAME),
            new Person(MENTION3_USERNAME));

        textView.setHashtagAdapter(defaultHashtagAdapter);
        textView.setMentionAdapter(defaultMentionAdapter);
        textView.setHashtagTextChangedListener(new SocialView.OnChangedListener() {
            @Override
            public void onChanged(@NonNull SocialView view, @NonNull CharSequence text) {
                Log.d("hashtag", text.toString());
            }
        });
        textView.setOnMentionClickListener(new SocialView.OnClickListener() {
            @Override
            public void onClick(@NonNull SocialView view, @NonNull CharSequence text) {
                Log.d("mention", text.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_demo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(!item.isChecked());
        switch (item.getItemId()) {
            case R.id.customAdapterItem:
                if (!item.isChecked()) {
                    textView.setHashtagAdapter(defaultHashtagAdapter);
                    textView.setMentionAdapter(defaultMentionAdapter);
                } else {
                    textView.setHashtagAdapter(customHashtagAdapter);
                    textView.setMentionAdapter(customMentionAdapter);
                }
                break;
            case R.id.enableHashtagItem:
                textView.setHashtagEnabled(item.isChecked());
                break;
            case R.id.enableMentionItem:
                textView.setMentionEnabled(item.isChecked());
                break;
            case R.id.enableHyperlinkItem:
                textView.setHyperlinkEnabled(item.isChecked());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    static class Person {
        final String name;

        Person(String name) {
            this.name = name;
        }
    }

    static class PersonAdapter extends SocialArrayAdapter<Person> {

        PersonAdapter(Context context) {
            super(context, R.layout.item_person, R.id.textViewName);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_person, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Person item = getItem(position);
            if (item != null) holder.textView.setText(item.name);
            return convertView;
        }

        private static class ViewHolder {
            final TextView textView;

            ViewHolder(@NonNull View view) {
                this.textView = view.findViewById(R.id.textViewName);
            }
        }
    }
}