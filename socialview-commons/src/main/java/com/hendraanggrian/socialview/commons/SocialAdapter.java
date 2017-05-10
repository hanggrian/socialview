package com.hendraanggrian.socialview.commons;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public abstract class SocialAdapter<T> extends ArrayAdapter<T> {

    private final List<T> tempItems;
    private final List<T> suggestions;

    public SocialAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId, new ArrayList<T>());
        this.tempItems = new ArrayList<>();
        this.suggestions = new ArrayList<>();
    }

    @Override
    public void add(@Nullable T item) {
        add(item, true);
    }

    @Override
    public void addAll(@NonNull Collection<? extends T> collection) {
        super.addAll(collection);
        tempItems.addAll(collection);
    }

    @Override
    @SafeVarargs
    public final void addAll(@NonNull T... items) {
        super.addAll(items);
        Collections.addAll(tempItems, items);
    }

    @Override
    public void remove(T object) {
        super.remove(object);
        tempItems.remove(object);
    }

    @Override
    public void clear() {
        clear(true);
    }

    private void add(@Nullable T item, boolean affectTempItems) {
        super.add(item);
        if (affectTempItems)
            tempItems.add(item);
    }

    private void clear(boolean affectTempItems) {
        super.clear();
        if (affectTempItems)
            tempItems.clear();
    }

    public abstract class SuggestionFilter extends Filter {

        public abstract String getString(T item);

        @Override
        @SuppressWarnings("unchecked")
        public CharSequence convertResultToString(Object resultValue) {
            return getString((T) resultValue);
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (T item : tempItems)
                    if (convertResultToString(item).toString().toLowerCase(Locale.US).contains(constraint.toString().toLowerCase(Locale.US)))
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
            List<T> filterList = (ArrayList<T>) results.values;
            if (results.count > 0) {
                clear(false);
                for (T item : filterList)
                    add(item, false);
                notifyDataSetChanged();
            }
        }
    }
}