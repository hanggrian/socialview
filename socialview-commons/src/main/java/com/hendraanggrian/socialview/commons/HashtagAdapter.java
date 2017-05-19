package com.hendraanggrian.socialview.commons;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.hendraanggrian.support.utils.view.Views;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public final class HashtagAdapter extends SocialAdapter<Hashtag> {

    @Nullable private Filter filter;

    public HashtagAdapter(@NonNull Context context) {
        super(context, R.layout.item_hashtag, R.id.textview_hashtag_value);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_hashtag, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Hashtag item = getItem(position);
        if (item != null) {
            holder.textViewHashtag.setText(item.getHashtag());
            if (Views.setVisible(holder.textViewHashtagCount, item.getCount() > -1)) {
                holder.textViewHashtagCount.setText(item.getCount() < 2
                        ? item.getCount() + " post"
                        : NumberFormat.getNumberInstance(Locale.US).format(item.getCount()) + " posts");
            }
        }
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new SuggestionFilter() {
                @Override
                public String getString(Hashtag item) {
                    return item.getHashtag();
                }
            };
        return filter;
    }

    private static class ViewHolder {
        @NonNull private final TextView textViewHashtag;
        @NonNull private final TextView textViewHashtagCount;

        private ViewHolder(@NonNull View view) {
            textViewHashtag = (TextView) view.findViewById(R.id.textview_hashtag_value);
            textViewHashtagCount = (TextView) view.findViewById(R.id.textview_hashtag_count);
        }
    }
}