package com.example.socialview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hendraanggrian.widget.SocialEditText;
import com.hendraanggrian.widget.socialview.OnSocialClickListener;
import com.hendraanggrian.widget.socialview.SocialTextWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class EditTextFragment extends Fragment {

    @BindView(R.id.socialedittext) SocialEditText editText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edittext, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        editText.setOnHashtagClickListener(new OnSocialClickListener() {
            @Override
            public void onClick(@NonNull TextView view, @NonNull CharSequence text) {
                Toast.makeText(getContext(), "#" + text, Toast.LENGTH_SHORT).show();
            }
        });
        editText.setOnMentionClickListener(new OnSocialClickListener() {
            @Override
            public void onClick(@NonNull TextView view, @NonNull CharSequence text) {
                Toast.makeText(getContext(), "@" + text, Toast.LENGTH_SHORT).show();
            }
        });

        editText.setHashtagTextChangedListener(new SocialTextWatcher() {
            @Override
            public void onTextChanged(@NonNull TextView view, @NonNull CharSequence text) {
                Log.d("#", text.toString());
            }
        });

        editText.setMentionTextChangedListener(new SocialTextWatcher() {
            @Override
            public void onTextChanged(@NonNull TextView view, @NonNull CharSequence text) {
                Log.d("@", text.toString());
            }
        });
    }
}