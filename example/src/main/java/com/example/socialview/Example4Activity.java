package com.example.socialview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.hendraanggrian.socialview.SocialTextWatcher;
import com.hendraanggrian.socialview.SociableView;
import com.hendraanggrian.socialview.commons.SocialAdapter;
import com.hendraanggrian.widget.SocialAutoCompleteTextView;

import butterknife.BindView;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public final class Example4Activity extends BaseActivity implements SocialTextWatcher {

    @BindView(R.id.toolbar_example3) Toolbar toolbar;
    @BindView(R.id.socialautocompletetextview_example3) SocialAutoCompleteTextView<Model, Model> socialAutoCompleteTextView;

    @Override
    protected int getContentView() {
        return R.layout.activity_example3;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);

        socialAutoCompleteTextView.setHashtagAdapter(new CustomAdapter(this));
        socialAutoCompleteTextView.setMentionAdapter(new CustomAdapter(this));
        socialAutoCompleteTextView.setSocialTextChangedListener(this);

        Model hashtag1 = new Model("follow");
        Model hashtag2 = new Model("followme");
        Model hashtag3 = new Model("followmeorillkillyou");
        socialAutoCompleteTextView.getHashtagAdapter().addAll(hashtag1, hashtag2, hashtag3);

        Model mention1 = new Model("dirtyhobo");
        Model mention2 = new Model("hobo");
        Model mention3 = new Model("hendraanggrian");
        socialAutoCompleteTextView.getMentionAdapter().addAll(mention1, mention2, mention3);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSocialTextChanged(@NonNull TextView v, @SociableView.Type int type, @NonNull CharSequence s) {
        Log.d("editing", String.format("%s - %s", type, s));
    }

    private static final class Model {
        private String content;

        private Model(String content) {
            this.content = content;
        }
    }

    private static final class CustomAdapter extends SocialAdapter<Model> {
        private Filter filter;

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
        public Filter getFilter() {
            if (filter == null)
                filter = new SuggestionFilter() {
                    @Override
                    public String getString(Model item) {
                        return item.content;
                    }
                };
            return filter;
        }

        private static class ViewHolder {
            private final TextView textView;

            private ViewHolder(@NonNull View view) {
                textView = (TextView) view.findViewById(R.id.textview_custom);
            }
        }
    }
}