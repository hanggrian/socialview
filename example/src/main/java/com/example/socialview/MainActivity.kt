package com.example.socialview

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button

import butterknife.BindViews

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class MainActivity : BaseActivity(), View.OnClickListener {

    @BindViews(R.id.button_main_example1, R.id.button_main_example2, R.id.button_main_example3, R.id.button_main_example4)
    lateinit var buttons: Array<Button>

    override val contentView: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        for (button in buttons)
            button.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_main_example1 -> startActivity(Intent(this, Example1Activity::class.java))
            R.id.button_main_example2 -> startActivity(Intent(this, Example2Activity::class.java))
            R.id.button_main_example3 -> startActivity(Intent(this, Example3Activity::class.java))
            R.id.button_main_example4 -> startActivity(Intent(this, Example4Activity::class.java))
        }
    }
}