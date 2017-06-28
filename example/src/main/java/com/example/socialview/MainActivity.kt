package com.example.socialview

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author Hendra Anggrian (com.hendraanggrian@gmail.com)
 */
class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        for (button in arrayOf(button1, button2, button3, button4)) {
            button.setOnClickListener(this)
        }
    }

    override fun onClick(v: View) {
        startActivity(Intent(this, when (v.id) {
            R.id.button1 -> Example1Activity::class.java
            R.id.button2 -> Example2Activity::class.java
            R.id.button3 -> Example3Activity::class.java
            else -> Example4Activity::class.java
        }))
    }
}