package io.github.hendraanggrian.socialviewdemo;

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

import io.github.hendraanggrian.socialview.SocialEditText;
import io.github.hendraanggrian.socialview.SocialView;
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
            public void onClick(@NonNull TextView view, @NonNull CharSequence text) {
                Toast.makeText(getContext(), "#" + text, Toast.LENGTH_SHORT).show();
            }
        });
        editText.setOnMentionClickListener(new SocialViewAttacher.OnSocialClickListener() {
            @Override
            public void onClick(@NonNull TextView view, @NonNull CharSequence text) {
                Toast.makeText(getContext(), "@" + text, Toast.LENGTH_SHORT).show();
            }
        });

        editText.setHashtagTextChangedListener(new SocialView.SocialTextWatcher() {
            @Override
            public void onTextChanged(@NonNull TextView view, @NonNull CharSequence text) {
                Log.d("#", text.toString());
            }
        });

        editText.setMentionTextChangedListener(new SocialView.SocialTextWatcher() {
            @Override
            public void onTextChanged(@NonNull TextView view, @NonNull CharSequence text) {
                Log.d("@", text.toString());
            }
        });
    }
}