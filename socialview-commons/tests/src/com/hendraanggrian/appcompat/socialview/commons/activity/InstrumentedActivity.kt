package com.hendraanggrian.appcompat.socialview.commons.activity

import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.hendraanggrian.appcompat.socialview.commons.test.R
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView

class InstrumentedActivity : AppCompatActivity() {
    lateinit var progressBar: ProgressBar
    lateinit var textView: SocialAutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instrumented)
        setSupportActionBar(findViewById(R.id.toolbar))
        progressBar = findViewById(R.id.progressBar)
        textView = findViewById(R.id.textView)
    }
}