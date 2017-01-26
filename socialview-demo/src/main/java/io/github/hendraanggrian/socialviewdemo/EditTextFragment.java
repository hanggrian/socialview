package io.github.hendraanggrian.socialviewdemo;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import io.github.hendraanggrian.socialview.SocialEditText;
import io.github.hendraanggrian.socialview.SocialViewAttacher;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class EditTextFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edittext, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SocialEditText editText = (SocialEditText) view.findViewById(R.id.socialedittext);
        editText.setOnHashtagClickListener(new SocialViewAttacher.OnSocialClickListener() {
            @Override
            public void onClick(@NonNull TextView view, @NonNull String clicked) {
                Toast.makeText(getContext(), "#" + clicked, Toast.LENGTH_SHORT).show();
            }
        });
        editText.setOnMentionClickListener(new SocialViewAttacher.OnSocialClickListener() {
            @Override
            public void onClick(@NonNull TextView view, @NonNull String clicked) {
                Toast.makeText(getContext(), "@" + clicked, Toast.LENGTH_SHORT).show();
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("onTextChanged", String.format("s=%s  start=%s    before=%s   count=%s", s, start, before, count));
                Log.d("test1", String.valueOf(s.charAt(start)));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}