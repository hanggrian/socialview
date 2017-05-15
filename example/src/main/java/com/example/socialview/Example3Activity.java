package com.example.socialview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.hendraanggrian.socialview.SocialTextWatcher;
import com.hendraanggrian.socialview.SociableView;
import com.hendraanggrian.socialview.commons.Hashtag;
import com.hendraanggrian.socialview.commons.HashtagAdapter;
import com.hendraanggrian.socialview.commons.Mention;
import com.hendraanggrian.socialview.commons.MentionAdapter;
import com.hendraanggrian.widget.SocialAutoCompleteTextView;

import butterknife.BindView;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public final class Example3Activity extends BaseActivity implements SocialTextWatcher {

    @BindView(R.id.toolbar_example3) Toolbar toolbar;
    @BindView(R.id.socialautocompletetextview_example3) SocialAutoCompleteTextView<Hashtag, Mention> socialAutoCompleteTextView;

    @Override
    protected int getContentView() {
        return R.layout.activity_example3;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);

        socialAutoCompleteTextView.setHashtagAdapter(new HashtagAdapter(this));
        socialAutoCompleteTextView.setMentionAdapter(new MentionAdapter(this));
        socialAutoCompleteTextView.setSocialTextChangedListener(this);

        Hashtag hashtag1 = new Hashtag("follow");
        Hashtag hashtag2 = new Hashtag("followme", 1000);
        Hashtag hashtag3 = new Hashtag("followmeorillkillyou", 500);
        socialAutoCompleteTextView.getHashtagAdapter().addAll(hashtag1, hashtag2, hashtag3);

        Mention mention1 = new Mention("dirtyhobo");
        Mention mention2 = new Mention("hobo", "Regular Hobo", R.mipmap.ic_launcher);
        Mention mention3 = new Mention("hendraanggrian", "Hendra Anggrian", "https://avatars0.githubusercontent.com/u/11507430?v=3&s=460");
        socialAutoCompleteTextView.getMentionAdapter().addAll(mention1, mention2, mention3);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSocialTextChanged(@NonNull TextView v, @SociableView.Type int type, @NonNull CharSequence s) {
        Log.d("editing", String.format("%s - %s", type, s));
    }
}