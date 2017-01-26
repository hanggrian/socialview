package io.github.hendraanggrian.socialview.commons;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public final class HashtagAdapter extends SuggestionAdapter<Hashtag> {

    public HashtagAdapter(@NonNull Context context) {
        super(context, R.layout.item_hashtag, R.id.textview_hashtag_value);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_hashtag, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Hashtag item = getItem(position);
        if (item != null) {
            holder.textViewHashtag.setText(item.getHashtag());

            if (item.getHashtagCount() != null) {
                holder.textViewHashtagCount.setText(item.getHashtagCount() == 1
                        ? "1 post"
                        : String.format("%s posts", NumberFormat.getNumberInstance(Locale.US).format(item.getHashtagCount())));
                holder.textViewHashtagCount.setVisibility(View.VISIBLE);
            } else {
                holder.textViewHashtagCount.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    @NonNull
    @Override
    public SuggestionFilter initializeFilter() {
        return new SuggestionFilter() {
            @Override
            public String getString(Hashtag item) {
                return item.getHashtag();
            }
        };
    }

    private static class ViewHolder {
        private final TextView textViewHashtag, textViewHashtagCount;

        private ViewHolder(@NonNull View view) {
            textViewHashtag = (TextView) view.findViewById(R.id.textview_hashtag_value);
            textViewHashtagCount = (TextView) view.findViewById(R.id.textview_hashtag_count);
        }
    }
}