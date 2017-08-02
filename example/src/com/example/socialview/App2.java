package com.example.socialview;

import android.app.Application;

import com.hendraanggrian.picasso.Picassos;
import com.squareup.picasso.Picasso;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class App2 extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Picassos.getCache(Picasso.with(this)).clear();
    }
}