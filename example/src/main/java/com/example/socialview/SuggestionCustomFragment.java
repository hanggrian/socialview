package com.example.socialview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.hendraanggrian.widget.SocialAutoCompleteTextView;
import com.hendraanggrian.widget.socialview.SocialTextWatcher;
import com.hendraanggrian.widget.socialview.SocialView;
import com.hendraanggrian.widget.socialview.commons.SocialAdapter;

import butterknife.BindView;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class SuggestionCustomFragment extends BaseFragment implements SocialTextWatcher {

    @BindView(R.id.socialsuggestionedittext) SocialAutoCompleteTextView<Model, Model> editText;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_suggestion;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editText.setHashtagAdapter(new CustomAdapter(getContext()));
        editText.setMentionAdapter(new CustomAdapter(getContext()));
        editText.setSocialTextChangedListener(this);

        Model hashtag1 = new Model("follow");
        Model hashtag2 = new Model("followme");
        Model hashtag3 = new Model("followmeorillkillyou");
        editText.getHashtagAdapter().addAll(hashtag1, hashtag2, hashtag3);

        Model mention1 = new Model("dirtyhobo");
        Model mention2 = new Model("hobo");
        Model mention3 = new Model("hendraanggrian");
        editText.getMentionAdapter().addAll(mention1, mention2, mention3);
    }

    @Override
    public void onTextChanged(@NonNull TextView v, @NonNull SocialView.Type type, @NonNull CharSequence s) {
        Log.d("editing", String.format("%s - %s", type.toString(), s));
    }

    private static final class Model {
        private String content;

        private Model(String content) {
            this.content = content;
        }
    }

    private static final class CustomAdapter extends SocialAdapter<Model> {
        private CustomAdapter(@NonNull Context context) {
            super(context, R.layout.item_custom, R.id.textview_custom);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_custom, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Model model = getItem(position);
            if (model != null) {
                holder.textView.setText(model.content);
            }
            return convertView;
        }

        @NonNull
        @Override
        public Filter initializeFilter() {
            return new SuggestionFilter() {
                @Override
                public String getString(Model item) {
                    return item.content;
                }
            };
        }

        private static class ViewHolder {
            private final TextView textView;

            private ViewHolder(@NonNull View view) {
                textView = (TextView) view.findViewById(R.id.textview_custom);
            }
        }
    }
}