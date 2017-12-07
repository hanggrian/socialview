package com.hendraanggrian.socialview.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ProgressBar
import com.hendraanggrian.socialview.test.R
import com.hendraanggrian.widget.SocialEditText
import kota.find

open class InstrumentedActivity : AppCompatActivity() {

    lateinit var progressBar: ProgressBar
    lateinit var editText: SocialEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instrumented)
        setSupportActionBar(find(R.id.toolbar))
        progressBar = find(R.id.progressBar)
        editText = find(R.id.editText)
    }
}