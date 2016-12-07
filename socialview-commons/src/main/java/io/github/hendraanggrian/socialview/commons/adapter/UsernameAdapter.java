package io.github.hendraanggrian.socialview.commons.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.hendraanggrian.socialview.commons.R;
import io.github.hendraanggrian.socialview.commons.model.Username;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class UsernameAdapter extends ArrayAdapter {

    @NonNull private final List<Username> list, filtered, all;
    @NonNull private final Filter filter;

    public UsernameAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        this.list = new ArrayList<>();
        this.filtered = new ArrayList<>();
        this.all = new ArrayList<>();
        this.filter = new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                List<Username> filteredList = (List<Username>) results.values;
                if (results.count > 0) {
                    clear();
                    for (Username people : filteredList) {
                        add(people);
                    }
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    filtered.clear();
                    for (Username username : all) {
                        if (username.getUsername().contains(constraint)) {
                            filtered.add(username);
                        }
                    }
                    filterResults.values = filtered;
                    filterResults.count = filtered.size();
                }
                return filterResults;
            }
        };
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Username getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_username, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageview_username);
            holder.textView = (TextView) convertView.findViewById(R.id.textview_username);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (getItem(position).getAvatar() instanceof String)
            Picasso.with(getContext()).load((String) getItem(position).getAvatar()).into(holder.imageView);
        else if (getItem(position).getAvatar() instanceof Drawable)
            holder.imageView.setImageDrawable((Drawable) getItem(position).getAvatar());
        holder.textView.setText(getItem(position).getUsername());
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    public void add(@NonNull Username... usernames) {
        list.addAll(Arrays.asList(usernames));
        all.addAll(Arrays.asList(usernames));
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        private ImageView imageView;
        private TextView textView;
    }
}