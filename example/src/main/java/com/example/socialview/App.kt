package com.example.socialview

import android.app.Application
import butterknife.ButterKnife
import com.hendraanggrian.socialview.SociableViewImpl

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ButterKnife.setDebug(BuildConfig.DEBUG)
        SociableViewImpl.setDebug(BuildConfig.DEBUG)
    }
}