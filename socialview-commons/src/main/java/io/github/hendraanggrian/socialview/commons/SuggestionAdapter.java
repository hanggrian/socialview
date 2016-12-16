package io.github.hendraanggrian.socialview.commons;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
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
public abstract class SuggestionAdapter<T> extends ArrayAdapter<T> {

    @NonNull
    public abstract View getView(int position, View convertView, @NonNull ViewGroup parent);

    @NonNull
    public abstract SuggestionFilter initializeFilter();

    @NonNull protected final List<T> tempItems;
    @NonNull protected final List<T> suggestions;
    @NonNull protected final SuggestionFilter filter;

    public SuggestionAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId, new ArrayList<T>());
        this.tempItems = new ArrayList<>();
        this.suggestions = new ArrayList<>();
        this.filter = initializeFilter();
    }

    @NonNull
    @Override
    public SuggestionFilter getFilter() {
        return filter;
    }

    @Override
    public void add(@NonNull T item) {
        add(item, true);
    }

    @Override
    public void addAll(@NonNull Collection<? extends T> collection) {
        tempItems.addAll(collection);
        super.addAll(collection);
    }

    @Override
    public void addAll(@NonNull T... items) {
        Collections.addAll(tempItems, items);
        super.addAll(items);
    }

    @Override
    public void remove(T object) {
        tempItems.remove(object);
        super.remove(object);
    }

    @Override
    public void clear() {
        clear(true);
    }

    protected void add(@NonNull T item, boolean affectTempItems) {
        if (affectTempItems)
            tempItems.add(item);
        super.add(item);
    }

    protected void clear(boolean affectTempItems) {
        if (affectTempItems)
            tempItems.clear();
        super.clear();
    }

    protected abstract class SuggestionFilter extends Filter {

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