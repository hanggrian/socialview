package io.github.hendraanggrian.socialview.commons;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public final class LabelAvatarAdapter extends ArrayAdapter<LabelAvatar> {

    @NonNull private final List<LabelAvatar> tempItems = new ArrayList<>();
    @NonNull private final List<LabelAvatar> suggestions = new ArrayList<>();
    @NonNull private final Filter filter = new LabelAvatarFilter();
    @NonNull private final Drawable defaultAvatar;

    public LabelAvatarAdapter(@NonNull Context context) {
        this(context, ContextCompat.getDrawable(context, R.drawable.ic_account_circle_black_48dp));
    }

    public LabelAvatarAdapter(@NonNull Context context, @NonNull Drawable defaultAvatar) {
        super(context, R.layout.item_label_avatar, R.id.textview_username, new ArrayList<>());
        this.defaultAvatar = defaultAvatar;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_label_avatar, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final LabelAvatar item = getItem(position);
        if (item != null) {
            if (item.avatar instanceof String)
                Picasso.with(getContext()).load((String) item.avatar).into(holder.imageView);
            else if (item.avatar instanceof Drawable)
                holder.imageView.setImageDrawable((Drawable) item.avatar);
            holder.textView.setText(item.label);
        }
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    public void add(String label) {
        final LabelAvatar item = new LabelAvatar(label, defaultAvatar);
        addAll(item);
        tempItems.add(item);
        notifyDataSetChanged();
    }

    /*public void add(@NonNull String username, @NonNull String avatarUrl) {
        Observable.just(original, all).subscribe(list -> list.add(new LabelAvatar(username, avatarUrl)),
                throwable -> {
                }, this::notifyDataSetChanged);
    }

    public void add(@NonNull String username, @NonNull Drawable avatarDrawable) {
        Observable.just(original, all).subscribe(list -> list.add(new LabelAvatar(username, avatarDrawable)),
                throwable -> {
                }, this::notifyDataSetChanged);
    }

    public void add(@NonNull String username, @DrawableRes int avatarDrawableRes) {
        Observable.just(original, all).subscribe(list -> list.add(new LabelAvatar(username, ContextCompat.getDrawable(getContext(), avatarDrawableRes))),
                throwable -> {
                }, this::notifyDataSetChanged);
    }

    @Override
    public void addAll(@NonNull String... usernames) {
        addAll(Arrays.asList(usernames));
    }

    @Override
    public void addAll(@NonNull Collection<? extends String> labels) {
        Observable.just(original, all).subscribe(list -> Observable.from(labels).forEach(label ->
                        list.add(new LabelAvatar(label, ContextCompat.getDrawable(getContext(), R.drawable.ic_account_circle_black_48dp)))),
                throwable -> {
                }, this::notifyDataSetChanged);
    }

    @Override
    public void remove(String label) {
        Observable.just(original, all).subscribe(list -> Observable.from(list)
                        .map(item -> item.label.equals(label))
                        .forEach(list::remove),
                throwable -> {
                }, this::notifyDataSetChanged);
    }

    @Override
    public void clear() {
        Observable.just(original, all)
                .subscribe(List::clear,
                        throwable -> {
                        }, this::notifyDataSetChanged);
    }*/

    private class LabelAvatarFilter extends Filter {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((LabelAvatar) resultValue).label;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (LabelAvatar item : tempItems)
                    if (item.label.toLowerCase().contains(constraint.toString().toLowerCase()))
                        suggestions.add(item);
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<LabelAvatar> filterList = (ArrayList<LabelAvatar>) results.values;
            if (results.count > 0) {
                clear();
                for (LabelAvatar item : filterList) {
                    add(item);
                    notifyDataSetChanged();
                }
            }
        }
    }

    private class ViewHolder {
        private ImageView imageView;
        private TextView textView;

        private ViewHolder(@NonNull View view) {
            imageView = (ImageView) view.findViewById(R.id.imageview_username);
            textView = (TextView) view.findViewById(R.id.textview_username);
        }
    }
}