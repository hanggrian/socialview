package com.example.socialview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.hendraanggrian.socialview.OnSocialClickListener;
import com.hendraanggrian.socialview.SocialTextWatcher;
import com.hendraanggrian.socialview.SocialView;
import com.hendraanggrian.widget.SocialEditText;

import butterknife.BindView;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public final class Example2Activity extends BaseActivity implements OnSocialClickListener, SocialTextWatcher {

    @BindView(R.id.toolbar_example2) Toolbar toolbar;
    @BindView(R.id.socialedittext_example2) SocialEditText socialEditText;

    @Override
    protected int getContentView() {
        return R.layout.activity_example2;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        socialEditText.setOnSocialClickListener(this);
        socialEditText.setSocialTextChangedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSocialTextChanged(@NonNull TextView v, @SocialView.Type int type, @NonNull CharSequence s) {
        Log.d("editing", String.format("%s - %s", type, s));
    }

    @Override
    public void onSocialClick(@NonNull TextView v, @SocialView.Type int type, @NonNull CharSequence text) {
        Toast.makeText(this, String.format("%s clicked:\n%s", type, text), Toast.LENGTH_SHORT).show();
    }
}