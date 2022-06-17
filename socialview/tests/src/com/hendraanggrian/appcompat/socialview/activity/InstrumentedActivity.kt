package com.hendraanggrian.appcompat.socialview.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hendraanggrian.appcompat.socialview.test.R
import com.hendraanggrian.appcompat.widget.SocialEditText

open class InstrumentedActivity : AppCompatActivity() {
    lateinit var editText: SocialEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instrumented)
        editText = findViewById(R.id.editText)
    }
}
