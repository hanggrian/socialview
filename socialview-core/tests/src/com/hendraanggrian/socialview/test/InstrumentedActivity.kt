package com.hendraanggrian.socialview.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.ProgressBar

import com.hendraanggrian.widget.SocialEditText

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class InstrumentedActivity : AppCompatActivity() {

    var progressBar: ProgressBar? = null
    var editText: SocialEditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instrumented)
        setSupportActionBar(findViewById(R.id.toolbar) as Toolbar)
        progressBar = findViewById(R.id.progressBar) as ProgressBar
        editText = findViewById(R.id.editText) as SocialEditText
    }
}