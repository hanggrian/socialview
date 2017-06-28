package com.example.socialview

import android.app.Application
import com.hendraanggrian.socialview.SociableViewImpl
import com.squareup.picasso.Picassos

/**
 * @author Hendra Anggrian (com.hendraanggrian@gmail.com)
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        SociableViewImpl.setDebug(BuildConfig.DEBUG)
        Picassos.getCache(this).clear()
    }
}