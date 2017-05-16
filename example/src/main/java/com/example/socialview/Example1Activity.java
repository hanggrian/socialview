package com.example.socialview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.hendraanggrian.socialview.OnSocialClickListener;
import com.hendraanggrian.socialview.SocialView;
import com.hendraanggrian.widget.SocialTextView;

import butterknife.BindView;
import butterknife.BindViews;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public final class Example1Activity extends BaseActivity implements OnSocialClickListener {

    @BindView(R.id.toolbar_example1) Toolbar toolbar;
    @BindViews({
            R.id.socialtextview_example1_1,
            R.id.socialtextview_example1_2,
            R.id.socialtextview_example1_3
    }) SocialTextView[] socialTextViews;

    @Override
    protected int getContentView() {
        return R.layout.activity_example1;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        for (SocialTextView textView : socialTextViews)
            textView.setOnSocialClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSocialClick(@NonNull TextView v, @SocialView.Type int type, @NonNull CharSequence text) {
        Toast.makeText(this, String.format("%s clicked:\n%s", type, text), Toast.LENGTH_SHORT).show();
    }
}