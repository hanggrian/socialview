package com.example.socialview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hendraanggrian.widget.SocialTextView;
import com.hendraanggrian.widget.socialview.OnSocialClickListener;
import com.hendraanggrian.widget.socialview.SocialView;

import butterknife.BindViews;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class TextViewFragment extends BaseFragment implements OnSocialClickListener {

    @BindViews({R.id.socialtextview1, R.id.socialtextview2, R.id.socialtextview3}) SocialTextView[] textViews;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_textview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for (SocialTextView textView : textViews)
            textView.setOnSocialClickListener(this);
    }

    @Override
    public void onClick(@NonNull TextView v, @NonNull SocialView.Type type, @NonNull CharSequence s) {
        Toast.makeText(getContext(), String.format("%s clicked:\n%s", type.toString(), s), Toast.LENGTH_SHORT).show();
    }
}