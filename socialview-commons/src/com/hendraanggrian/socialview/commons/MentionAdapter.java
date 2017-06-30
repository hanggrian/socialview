package com.hendraanggrian.socialview.commons;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hendraanggrian.picasso.commons.target.Targets;
import com.hendraanggrian.picasso.commons.transformation.Transformations;
import com.hendraanggrian.support.utils.content.Resources2;
import com.hendraanggrian.support.utils.view.Views;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;

/**
 * Default adapter for displaying mention in {@link com.hendraanggrian.widget.SocialAutoCompleteTextView}.
 *
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class MentionAdapter extends SocialAdapter<Mention> {

    @DrawableRes private final int defaultAvatar;
    private final Filter filter = new SocialFilter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Mention) resultValue).getUsername();
        }
    };

    public MentionAdapter(@NonNull Context context) {
        this(context, R.drawable.ic_placeholder_mention);
    }

    public MentionAdapter(@NonNull Context context, @DrawableRes int defaultAvatar) {
        super(context, R.layout.widget_socialview_mention, R.id.textViewUsername);
        this.defaultAvatar = defaultAvatar;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.widget_socialview_mention, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Mention item = getItem(position);
        if (item != null) {
            Picasso picasso = Picasso.with(getContext());
            RequestCreator request = null;
            if (item.getAvatar() == null) {
                request = picasso.load(defaultAvatar);
            } else if (item.getAvatar() instanceof Integer) {
                request = picasso.load((Integer) item.getAvatar());
            } else if (item.getAvatar() instanceof String) {
                request = picasso.load((String) item.getAvatar());
            } else if (item.getAvatar() instanceof Uri) {
                request = picasso.load((Uri) item.getAvatar());
            } else if (item.getAvatar() instanceof File) {
                request = picasso.load((File) item.getAvatar());
            }

            if (request != null) {
                int progressBarSize = Resources2.toPx(24);
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(progressBarSize, progressBarSize);
                lp.gravity = Gravity.CENTER;
                ProgressBar progressBar = new ProgressBar(getContext());
                progressBar.setLayoutParams(lp);
                request.error(defaultAvatar)
                        .transform(Transformations.circle())
                        .into(Targets.placeholder(holder.imageView, progressBar));
            }

            holder.textViewUsername.setText(item.getUsername());
            if (Views.setVisible(holder.textViewDisplayname, !TextUtils.isEmpty(item.getDisplayname()))) {
                holder.textViewDisplayname.setText(item.getDisplayname());
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
        @NonNull private final ImageView imageView;
        @NonNull private final TextView textViewUsername;
        @NonNull private final TextView textViewDisplayname;

        private ViewHolder(@NonNull View view) {
            imageView = (ImageView) view.findViewById(R.id.imageView);
            textViewUsername = (TextView) view.findViewById(R.id.textViewUsername);
            textViewDisplayname = (TextView) view.findViewById(R.id.textViewDisplayname);
        }
    }
}