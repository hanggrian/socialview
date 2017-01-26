package io.github.hendraanggrian.socialviewdemo;


import android.os.Bundle;
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
        editText.setOnHashtagEditingListener(new SocialView.OnSocialEditingListener() {
            @Override
            public void onEditing(TextView view, String text) {
                Log.d("#", text);
            }
        });
        editText.setOnMentionEditingListener(new SocialView.OnSocialEditingListener() {
            @Override
            public void onEditing(TextView view, String text) {
                Log.d("@", text);
            }
        });

        // use default item Hashtag
        editText.getHashtagAdapter().add(new Hashtag("follow"));
        // a hashtag can have count
        //editText.getHashtagAdapter().add(new Hashtag("followme", 1000));
        // or use custom model implementing Hashtagable
        //editText.getHashtagAdapter().add(new Interest("followmeorillkillyou", 500));

        // use default item Mention
        editText.getMentionAdapter().add(new Mention("dirtyhobo"));
        // a mention can have display name and avatar (can be drawable or string url)
        //editText.getMentionAdapter().add(new Mention("hobo", "Regular Hobo", R.mipmap.ic_launcher));
        // or use custom model implementing Mentionable
        //editText.getMentionAdapter().add(new User("hendraanggrian", "Hendra Anggrian", "https://avatars0.githubusercontent.com/u/11507430?v=3&s=460"));
    }
}