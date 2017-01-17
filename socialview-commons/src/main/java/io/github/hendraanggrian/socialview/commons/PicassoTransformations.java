package io.github.hendraanggrian.socialview.commons;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.squareup.picasso.Transformation;

import java.lang.ref.WeakReference;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 * @see com.squareup.picasso.Picasso
 * @see Transformation
 */
final class PicassoTransformations {

    private static WeakReference<Transformation> circular;

    static Transformation circular() {
        if (circular != null && circular.get() != null)
            return circular.get();

        circular = new WeakReference<Transformation>(new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                final int size = Math.min(source.getWidth(), source.getHeight());
                final int x = (source.getWidth() - size) / 2;
                final int y = (source.getHeight() - size) / 2;
                final Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
                if (squaredBitmap != source)
                    source.recycle();

                final Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

                final Paint paint = new Paint();
                paint.setShader(new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
                paint.setAntiAlias(true);

                final float r = size / 2f;
                final Canvas canvas = new Canvas(bitmap);
                canvas.drawCircle(r, r, r, paint);

                squaredBitmap.recycle();
                return bitmap;
            }

            @Override
            public String key() {
                return "circular";
            }
        });
        return circular();
    }
}