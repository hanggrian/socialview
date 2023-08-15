package com.hendraanggrian.appcompat.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.PluralsRes;

import com.hendraanggrian.appcompat.socialview.Hashtagable;
import com.hendraanggrian.appcompat.socialview.autocomplete.R;

/**
 * Default adapter for displaying hashtag in {@link SocialAutoCompleteTextView}. Note that this
 * adapter is completely optional, any adapter extending {@link android.widget.ArrayAdapter} can be
 * attached to {@link SocialAutoCompleteTextView}.
 */
public class HashtagArrayAdapter<T extends Hashtagable> extends SocialArrayAdapter<T> {
  private final int countPluralRes;

  public HashtagArrayAdapter(@NonNull Context context) {
    this(context, R.plurals.posts);
  }

  public HashtagArrayAdapter(@NonNull Context context, @PluralsRes int countPluralRes) {
    super(context, R.layout.socialview_layout_hashtag, R.id.socialview_hashtag);
    this.countPluralRes = countPluralRes;
  }

  @NonNull
  @Override
  @SuppressWarnings("unchecked")
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    final ViewHolder holder;
    if (convertView == null) {
      convertView =
          LayoutInflater.from(getContext())
              .inflate(R.layout.socialview_layout_hashtag, parent, false);
      holder = new ViewHolder(convertView);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }
    final T item = getItem(position);
    if (item != null) {
      holder.hashtagView.setText(item.getId());

      if (item.getCount() > 0) {
        holder.countView.setVisibility(View.VISIBLE);
        final int count = item.getCount();
        holder.countView.setText(
            getContext().getResources().getQuantityString(countPluralRes, count, count));
      } else {
        holder.countView.setVisibility(View.GONE);
      }
    }
    return convertView;
  }

  private static class ViewHolder {
    private final TextView hashtagView;
    private final TextView countView;

    ViewHolder(View itemView) {
      hashtagView = itemView.findViewById(R.id.socialview_hashtag);
      countView = itemView.findViewById(R.id.socialview_hashtag_count);
    }
  }
}
