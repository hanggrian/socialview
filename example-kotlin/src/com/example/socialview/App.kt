package com.example.socialview

import android.app.Application
import com.squareup.picasso.Picasso
import com.squareup.picasso.getCache

/**
 * @author Hendra Anggrian (com.hendraanggrian@gmail.com)
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Picasso.with(this).getCache().clear()
    }
}