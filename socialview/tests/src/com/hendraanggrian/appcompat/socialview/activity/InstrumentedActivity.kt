package com.hendraanggrian.appcompat.socialview.activity

import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.hendraanggrian.appcompat.socialview.test.R
import com.hendraanggrian.appcompat.widget.SocialEditText

open class InstrumentedActivity : AppCompatActivity() {

    lateinit var progressBar: ProgressBar
    lateinit var editText: SocialEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instrumented)
        setSupportActionBar(findViewById(R.id.toolbar))
        progressBar = findViewById(R.id.progressBar)
        editText = findViewById(R.id.editText)
    }
}