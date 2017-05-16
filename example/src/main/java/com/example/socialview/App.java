package com.example.socialview;

import android.app.Application;

import com.hendraanggrian.socialview.SocialView;

import butterknife.ButterKnife;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ButterKnife.setDebug(BuildConfig.DEBUG);
        SocialView.setDebug(BuildConfig.DEBUG);
    }
}