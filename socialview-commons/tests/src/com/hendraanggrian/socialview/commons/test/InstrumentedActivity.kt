package com.hendraanggrian.socialview.commons.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.ProgressBar

import com.hendraanggrian.widget.SocialAutoCompleteTextView

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class InstrumentedActivity : AppCompatActivity() {

    lateinit var progressBar: ProgressBar
    lateinit var textView: SocialAutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instrumented)
        setSupportActionBar(findViewById(R.id.toolbar) as Toolbar)
        progressBar = findViewById(R.id.progressBar) as ProgressBar
        textView = findViewById(R.id.textView) as SocialAutoCompleteTextView
    }
}