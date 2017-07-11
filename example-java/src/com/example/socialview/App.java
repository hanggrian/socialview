package com.example.socialview;

import android.app.Application;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassosKt;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PicassosKt.getCache(Picasso.with(this)).clear();
    }
}