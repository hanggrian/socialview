package com.hendraanggrian.socialview.commons;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.hendraanggrian.support.utils.view.Views;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Default adapter for displaying hashtag in {@link com.hendraanggrian.widget.SocialAutoCompleteTextView}.
 *
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class HashtagAdapter extends SocialAdapter<Hashtag> {

    private final Filter filter = new SocialFilter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Hashtag) resultValue).getHashtag();
        }
    };

    public HashtagAdapter(@NonNull Context context) {
        super(context, R.layout.widget_socialview_hashtag, R.id.textViewHashtag);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.widget_socialview_hashtag, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Hashtag item = getItem(position);
        if (item != null) {
            holder.textViewHashtag.setText(item.getHashtag());
            if (Views.setVisible(holder.textViewCount, item.getCount() > -1)) {
                holder.textViewCount.setText(item.getCount() < 2
                        ? item.getCount() + " post"
                        : NumberFormat.getNumberInstance(Locale.US).format(item.getCount()) + " posts");
            }
        }
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    private static class ViewHolder {
        @NonNull private final TextView textViewHashtag;
        @NonNull private final TextView textViewCount;

        private ViewHolder(@NonNull View view) {
            textViewHashtag = (TextView) view.findViewById(R.id.textViewHashtag);
            textViewCount = (TextView) view.findViewById(R.id.textViewCount);
        }
    }
}