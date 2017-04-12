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

import com.hendraanggrian.widget.SocialAutoCompleteTextView;
import com.hendraanggrian.widget.socialview.SocialTextWatcher;
import com.hendraanggrian.widget.socialview.commons.Hashtag;
import com.hendraanggrian.widget.socialview.commons.HashtagAdapter;
import com.hendraanggrian.widget.socialview.commons.Mention;
import com.hendraanggrian.widget.socialview.commons.MentionAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class SuggestionFragment extends Fragment {

    @BindView(R.id.socialsuggestionedittext) SocialAutoCompleteTextView<Hashtag, Mention> editText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_suggestion, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        editText.setHashtagAdapter(new HashtagAdapter(getContext())); // or use custom adapter extending SuggestionAdapter<Hashtagable>
        editText.setMentionAdapter(new MentionAdapter(getContext())); // or use custom adapter extending SuggestionAdapter<Mentionable>
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

        Hashtag hashtag1 = new Hashtag("follow");
        editText.getHashtagAdapter().add(hashtag1);

        Hashtag hashtag2 = new Hashtag("followme");
        hashtag2.setCount(1000);
        editText.getHashtagAdapter().add(hashtag2);

        Hashtag hashtag3 = new Hashtag("followmeorillkillyou");
        hashtag3.setCount(500);
        editText.getHashtagAdapter().add(hashtag3);

        Mention mention1 = new Mention("dirtyhobo");
        editText.getMentionAdapter().add(mention1);

        Mention mention2 = new Mention("hobo");
        mention2.setDisplayname("Regular Hobo");
        mention2.setAvatar(R.mipmap.ic_launcher);
        editText.getMentionAdapter().add(mention2);

        Mention mention3 = new Mention("hendraanggrian");
        mention3.setDisplayname("Hendra Anggrian");
        mention3.setAvatar("https://avatars0.githubusercontent.com/u/11507430?v=3&s=460");
        editText.getMentionAdapter().add(mention3);
    }
}