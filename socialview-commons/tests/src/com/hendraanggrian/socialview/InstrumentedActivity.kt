package com.hendraanggrian.socialview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ProgressBar
import com.hendraanggrian.socialview.commons.test.R
import com.hendraanggrian.widget.SocialAutoCompleteTextView
import kota.find

class InstrumentedActivity : AppCompatActivity() {

    lateinit var progressBar: ProgressBar
    lateinit var textView: SocialAutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instrumented)
        setSupportActionBar(find(R.id.toolbar))
        progressBar = find(R.id.progressBar)
        textView = find(R.id.textView)
    }
}