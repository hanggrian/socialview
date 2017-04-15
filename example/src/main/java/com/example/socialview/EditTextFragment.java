package com.example.socialview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hendraanggrian.widget.SocialEditText;
import com.hendraanggrian.widget.socialview.OnSocialClickListener;
import com.hendraanggrian.widget.socialview.SocialTextWatcher;
import com.hendraanggrian.widget.socialview.SocialView;

import butterknife.BindView;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class EditTextFragment extends BaseFragment implements OnSocialClickListener, SocialTextWatcher {

    @BindView(R.id.socialedittext) SocialEditText editText;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_edittext;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText.setOnSocialClickListener(this);
        editText.setSocialTextChangedListener(this);
    }

    @Override
    public void onClick(@NonNull TextView v, @NonNull SocialView.Type type, @NonNull CharSequence s) {
        Toast.makeText(getContext(), String.format("%s clicked:\n%s", type.toString(), s), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTextChanged(@NonNull TextView v, @NonNull SocialView.Type type, @NonNull CharSequence s) {
        Log.d("editing", String.format("%s - %s", type.toString(), s));
    }
}