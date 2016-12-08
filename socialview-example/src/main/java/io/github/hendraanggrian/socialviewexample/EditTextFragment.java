package io.github.hendraanggrian.socialviewexample;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import io.github.hendraanggrian.socialview.SocialEditText;
import io.github.hendraanggrian.socialview.SocialView;

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
        editText.setOnHashtagClickListener(new SocialView.OnSocialClickListener() {
            @Override
            public void onClick(View view, String clicked) {
                Toast.makeText(getContext(), "#" + clicked, Toast.LENGTH_SHORT).show();
            }
        });
        editText.setOnMentionClickListener(new SocialView.OnSocialClickListener() {
            @Override
            public void onClick(View view, String clicked) {
                Toast.makeText(getContext(), "@" + clicked, Toast.LENGTH_SHORT).show();
            }
        });
    }
}