package com.hendraanggrian.appcompat.widget;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * An ArrayAdapter customized with filter to display items.
 * It is a direct parent of default {@link HashtagArrayAdapter} and {@link MentionArrayAdapter},
 * which are optional adapters.
 */
public abstract class SocialArrayAdapter<T> extends ArrayAdapter<T> {

    private Filter filter;
    private final List<T> items = new ArrayList<>();
    private final List<T> tempItems = new ArrayList<>();

    public SocialArrayAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    @Override
    public void add(@Nullable T object) {
        add(object, true);
    }

    @Override
    public void addAll(@NonNull Collection<? extends T> collection) {
        super.addAll(collection);
        tempItems.addAll(collection);
    }

    @SafeVarargs
    @Override
    public final void addAll(T... items) {
        super.addAll(items);
        Collections.addAll(tempItems, items);
    }

    @Override
    public void remove(@Nullable T object) {
        super.remove(object);
        tempItems.remove(object);
    }

    @Override
    public void clear() {
        clear(true);
    }

    @NonNull
    public CharSequence convertToString(T object) {
        return object.toString();
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public Filter getFilter() {
        if (filter == null) {
            filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    if (constraint == null) {
                        return new FilterResults();
                    } else {
                        items.clear();
                        for (final T item : tempItems) {
                            if (convertResultToString(item)
                                .toString()
                                .toLowerCase(Locale.getDefault())
                                .contains(constraint.toString().toLowerCase(Locale.getDefault()))) {
                                items.add(item);
                            }
                        }
                        final FilterResults results = new FilterResults();
                        results.values = items;
                        results.count = items.size();
                        return results;
                    }
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    final List<T> list = (List<T>) results;
                    if (results.count > 0) {
                        clear(false);
                        for (final T object : list) {
                            add(object, false);
                        }
                        notifyDataSetChanged();
                    }
                }

                @Override
                public CharSequence convertResultToString(Object resultValue) {
                    return convertToString((T) resultValue);
                }
            };
        }
        return filter;
    }

    private void add(T object, boolean affectTempItems) {
        super.add(object);
        if (affectTempItems) {
            tempItems.add(object);
        }
    }

    private void clear(boolean affectTempItems) {
        super.clear();
        if (affectTempItems) {
            tempItems.clear();
        }
    }
}