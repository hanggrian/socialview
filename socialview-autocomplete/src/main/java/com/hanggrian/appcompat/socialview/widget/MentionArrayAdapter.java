package com.hanggrian.appcompat.socialview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.hanggrian.appcompat.socialview.autocomplete.Mentionable;
import com.hanggrian.appcompat.socialview.autocomplete.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;
import java.io.File;

/**
 * Default adapter for displaying mention in {@link SocialAutoCompleteTextView}. Note that this
 * adapter is completely optional, any adapter extending {@link android.widget.ArrayAdapter} can be
 * attached to {@link SocialAutoCompleteTextView}.
 */
public class MentionArrayAdapter<T extends Mentionable> extends SocialArrayAdapter<T> {
    private final int defaultAvatar;

    public MentionArrayAdapter(@NonNull Context context) {
        this(context, R.drawable.socialview_ic_mention_placeholder);
    }

    public MentionArrayAdapter(@NonNull Context context, @DrawableRes int defaultAvatar) {
        super(context, R.layout.socialview_layout_mention, R.id.socialview_mention_username);
        this.defaultAvatar = defaultAvatar;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView =
                LayoutInflater.from(getContext())
                    .inflate(R.layout.socialview_layout_mention, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final T item = getItem(position);
        if (item != null) {
            holder.usernameView.setText(item.getUsername());

            final CharSequence displayname = item.getDisplayname();
            if (!TextUtils.isEmpty(displayname)) {
                holder.displaynameView.setText(displayname);
                holder.displaynameView.setVisibility(View.VISIBLE);
            } else {
                holder.displaynameView.setVisibility(View.GONE);
            }

            final Object avatar = item.getAvatar();
            final RequestCreator request;
            if (avatar == null) {
                request = Picasso.get().load(defaultAvatar);
            } else if (avatar instanceof Integer) {
                request = Picasso.get().load((int) avatar);
            } else if (avatar instanceof String) {
                request = Picasso.get().load((String) avatar);
            } else if (avatar instanceof Uri) {
                request = Picasso.get().load((Uri) avatar);
            } else if (avatar instanceof File) {
                request = Picasso.get().load((File) avatar);
            } else {
                throw new UnsupportedOperationException("Unknown avatar type");
            }
            request
                .error(defaultAvatar)
                .transform(new CropCircleTransformation())
                .into(
                    holder.avatarView,
                    new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.loadingView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            holder.loadingView.setVisibility(View.GONE);
                        }
                    }
                );
        }
        return convertView;
    }

    private static class ViewHolder {
        private final ImageView avatarView;
        private final ProgressBar loadingView;
        private final TextView usernameView;
        private final TextView displaynameView;

        ViewHolder(View itemView) {
            avatarView = itemView.findViewById(R.id.socialview_mention_avatar);
            loadingView = itemView.findViewById(R.id.socialview_mention_loading);
            usernameView = itemView.findViewById(R.id.socialview_mention_username);
            displaynameView = itemView.findViewById(R.id.socialview_mention_displayname);
        }
    }

    private static class CropCircleTransformation implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            final int size = Math.min(source.getWidth(), source.getHeight());
            final int width = (source.getWidth() - size) / 2;
            final int height = (source.getHeight() - size) / 2;
            final Bitmap target = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            final float r = size / 2f;

            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
            // source isn't square, move viewport to center
            if (width != 0 || height != 0) {
                final Matrix matrix = new Matrix();
                matrix.setTranslate(-width, -height);
                paint.getShader().setLocalMatrix(matrix);
            }
            new Canvas(target).drawCircle(r, r, r, paint);

            source.recycle();
            return target;
        }

        @Override
        public String key() {
            return "CropCircleTransformation()";
        }
    }
}
