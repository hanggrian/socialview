package com.hendraanggrian.socialview.commons;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hendraanggrian.picasso.commons.transformation.Transformations;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public final class MentionAdapter extends SocialAdapter<Mention> {

    @DrawableRes private final int defaultAvatar;

    public MentionAdapter(@NonNull Context context) {
        this(context, R.drawable.ic_placeholder_mention);
    }

    public MentionAdapter(@NonNull Context context, @DrawableRes int defaultAvatar) {
        super(context, R.layout.item_mention, R.id.textview_mention_username);
        this.defaultAvatar = defaultAvatar;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_mention, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Mention item = getItem(position);
        if (item != null) {
            holder.textViewUsername.setText(item.getUsername());

            Picasso picasso = Picasso.with(getContext());
            RequestCreator request = null;
            if (item.getAvatar() == null)
                request = picasso.load(defaultAvatar);
            else if (item.getAvatar() instanceof Integer)
                request = picasso.load((int) item.getAvatar());
            else if (item.getAvatar() instanceof String)
                request = picasso.load((String) item.getAvatar())
                        .placeholder(defaultAvatar)
                        .error(defaultAvatar);
            if (request != null)
                request.transform(Transformations.circle())
                        .fit()
                        .into(holder.imageView);

            if (!TextUtils.isEmpty(item.getDisplayname())) {
                holder.textViewDisplayname.setText(item.getDisplayname());
                holder.textViewDisplayname.setVisibility(View.VISIBLE);
            } else {
                holder.textViewDisplayname.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    @NonNull
    @Override
    public Filter initializeFilter() {
        return new SuggestionFilter() {
            @Override
            public String getString(Mention item) {
                return item.getUsername();
            }
        };
    }

    private static class ViewHolder {
        @NonNull private final ImageView imageView;
        @NonNull private final TextView textViewUsername;
        @NonNull private final TextView textViewDisplayname;

        private ViewHolder(@NonNull View view) {
            imageView = (ImageView) view.findViewById(R.id.imageview_mention_username);
            textViewUsername = (TextView) view.findViewById(R.id.textview_mention_username);
            textViewDisplayname = (TextView) view.findViewById(R.id.textview_mention_displayname);
        }
    }
}