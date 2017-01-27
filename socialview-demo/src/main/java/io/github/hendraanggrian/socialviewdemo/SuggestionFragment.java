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

import io.github.hendraanggrian.socialview.SocialAutoCompleteTextView;
import io.github.hendraanggrian.socialview.SocialView;
import io.github.hendraanggrian.socialview.commons.Hashtag;
import io.github.hendraanggrian.socialview.commons.HashtagAdapter;
import io.github.hendraanggrian.socialview.commons.Mention;
import io.github.hendraanggrian.socialview.commons.MentionAdapter;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class SuggestionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_suggestion, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SocialAutoCompleteTextView<Hashtag, Mention> editText = (SocialAutoCompleteTextView<Hashtag, Mention>) view.findViewById(R.id.socialsuggestionedittext);
        editText.setHashtagAdapter(new HashtagAdapter(getContext())); // or use custom adapter extending SuggestionAdapter<Hashtagable>
        editText.setMentionAdapter(new MentionAdapter(getContext())); // or use custom adapter extending SuggestionAdapter<Mentionable>
        editText.setHashtagTextChangedListener(new SocialView.SocialTextWatcher() {
            @Override
            public void onTextChanged(@NonNull TextView view, @NonNull String s) {
                Log.d("#", s);
            }
        });
        editText.setMentionTextChangedListener(new SocialView.SocialTextWatcher() {
            @Override
            public void onTextChanged(@NonNull TextView view, @NonNull String s) {
                Log.d("@", s);
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