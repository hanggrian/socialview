package com.example.socialview;

import android.app.Application;

import com.hendraanggrian.socialview.SocialViewImpl;

import butterknife.ButterKnife;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ButterKnife.setDebug(BuildConfig.DEBUG);
        SocialViewImpl.setDebug(BuildConfig.DEBUG);
    }
}