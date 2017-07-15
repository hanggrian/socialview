package com.hendraanggrian.socialview.core.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.ProgressBar
import com.hendraanggrian.kota.view.findViewBy
import com.hendraanggrian.socialview.test.R

import com.hendraanggrian.widget.SocialEditText

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class InstrumentedActivity : AppCompatActivity() {

    lateinit var progressBar: ProgressBar
    lateinit var editText: SocialEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instrumented)
        setSupportActionBar(findViewBy<Toolbar>(R.id.toolbar))
        progressBar = findViewBy(R.id.progressBar)
        editText = findViewBy(R.id.editText)
    }
}