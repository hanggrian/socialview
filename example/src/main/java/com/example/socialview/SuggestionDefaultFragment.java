package com.example.socialview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hendraanggrian.widget.SocialAutoCompleteTextView;
import com.hendraanggrian.socialview.SocialTextWatcher;
import com.hendraanggrian.socialview.SocialView;
import com.hendraanggrian.socialview.commons.Hashtag;
import com.hendraanggrian.socialview.commons.HashtagAdapter;
import com.hendraanggrian.socialview.commons.Mention;
import com.hendraanggrian.socialview.commons.MentionAdapter;

import butterknife.BindView;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class SuggestionDefaultFragment extends BaseFragment implements SocialTextWatcher {

    @BindView(R.id.socialsuggestionedittext) SocialAutoCompleteTextView<Hashtag, Mention> editText;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_suggestion;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editText.setHashtagAdapter(new HashtagAdapter(getContext()));
        editText.setMentionAdapter(new MentionAdapter(getContext()));
        editText.setSocialTextChangedListener(this);

        Hashtag hashtag1 = new Hashtag("follow");
        Hashtag hashtag2 = new Hashtag("followme", 1000);
        Hashtag hashtag3 = new Hashtag("followmeorillkillyou", 500);
        editText.getHashtagAdapter().addAll(hashtag1, hashtag2, hashtag3);

        Mention mention1 = new Mention("dirtyhobo");
        Mention mention2 = new Mention("hobo", "Regular Hobo", R.mipmap.ic_launcher);
        Mention mention3 = new Mention("hendraanggrian", "Hendra Anggrian", "https://avatars0.githubusercontent.com/u/11507430?v=3&s=460");
        editText.getMentionAdapter().addAll(mention1, mention2, mention3);
    }

    @Override
    public void onTextChanged(@NonNull TextView v, @NonNull SocialView.Type type, @NonNull CharSequence s) {
        Log.d("editing", String.format("%s - %s", type.toString(), s));
    }
}