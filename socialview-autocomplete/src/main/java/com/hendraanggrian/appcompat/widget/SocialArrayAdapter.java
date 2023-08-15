package com.hendraanggrian.appcompat.widget;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * An {@link ArrayAdapter} customized with filter to display items. It is a direct parent of default
 * {@link HashtagArrayAdapter} and {@link MentionArrayAdapter}, which are optional adapters.
 */
public abstract class SocialArrayAdapter<T> extends ArrayAdapter<T> {
  private Filter filter;
  private final List<T> tempItems = new ArrayList<>();

  public SocialArrayAdapter(@NonNull Context context, int resource, int textViewResourceId) {
    super(context, resource, textViewResourceId);
  }

  @Override
  public void add(@Nullable T object) {
    super.add(object);
    tempItems.add(object);
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
    super.clear();
    tempItems.clear();
  }

  @NonNull
  public CharSequence convertToString(T object) {
    return object.toString();
  }

  @NonNull
  @Override
  public Filter getFilter() {
    if (filter == null) {
      filter = new SocialFilter();
    }
    return filter;
  }

  @SuppressWarnings("unchecked")
  private class SocialFilter extends Filter {
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
      final FilterResults results = new FilterResults();
      if (TextUtils.isEmpty(constraint)) {
        return results;
      }
      final List<T> filteredItems = new ArrayList<>();
      for (final T item : tempItems) {
        if (convertResultToString(item)
            .toString()
            .toLowerCase(Locale.getDefault())
            .contains(constraint.toString().toLowerCase(Locale.getDefault()))) {
          filteredItems.add(item);
        }
      }
      results.values = filteredItems;
      results.count = filteredItems.size();
      return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
      if (results.values instanceof List) {
        final List<T> list = (List<T>) results.values;
        if (results.count > 0) {
          SocialArrayAdapter.super.clear();
          for (final T object : list) {
            SocialArrayAdapter.super.add(object);
          }
          notifyDataSetChanged();
        }
      }
    }

    @Override
    public CharSequence convertResultToString(Object resultValue) {
      return convertToString((T) resultValue);
    }
  }
}
