package com.example.socialview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.hendraanggrian.socialview.OnSocialClickListener
import com.hendraanggrian.socialview.SociableView
import com.hendraanggrian.support.utils.widget.Toasts
import kotlinx.android.synthetic.main.activity_example1.*

/**
 * @author Hendra Anggrian (com.hendraanggrian@gmail.com)
 */
class Example1Activity : AppCompatActivity(), OnSocialClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example1)
        setSupportActionBar(toolbar)
        for (textView in arrayOf(textView1, textView2, textView3)) {
            textView.setOnHashtagClickListener(this)
            textView.setOnMentionClickListener(this)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onSocialClick(v: SociableView, text: CharSequence) {
        Toasts.showShort(this, String.format("clicked:\n%s", text))
    }
}