package com.example.socialview

import android.app.Application
import com.squareup.picasso.Picassos

/**
 * @author Hendra Anggrian (com.hendraanggrian@gmail.com)
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Picassos.getCache(this).clear()
    }
}