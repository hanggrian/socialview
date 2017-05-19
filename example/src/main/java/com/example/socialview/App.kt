package com.example.socialview

import android.app.Application
import butterknife.ButterKnife
import com.hendraanggrian.socialview.SocialView

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ButterKnife.setDebug(BuildConfig.DEBUG)
        SocialView.setDebug(BuildConfig.DEBUG)
    }
}