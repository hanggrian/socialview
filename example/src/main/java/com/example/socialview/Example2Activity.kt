package com.example.socialview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.hendraanggrian.socialview.OnSocialClickListener
import com.hendraanggrian.socialview.SociableView
import com.hendraanggrian.socialview.SocialTextWatcher
import com.hendraanggrian.support.utils.util.Logs
import com.hendraanggrian.support.utils.widget.Toasts
import kotlinx.android.synthetic.main.activity_example2.*

/**
 * @author Hendra Anggrian (com.hendraanggrian@gmail.com)
 */
class Example2Activity : AppCompatActivity(), OnSocialClickListener, SocialTextWatcher {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example2)
        setSupportActionBar(toolbar)
        editText.setOnHashtagClickListener(this)
        editText.setOnMentionClickListener(this)
        editText.setHashtagTextChangedListener(this)
        editText.setMentionTextChangedListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onSocialTextChanged(v: SociableView, s: CharSequence) {
        Logs.d("editing", s)
    }

    override fun onSocialClick(v: SociableView, text: CharSequence) {
        Toasts.showShort(this, String.format("clicked:\n%s", text))
    }
}