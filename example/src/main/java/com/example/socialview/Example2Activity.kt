package com.example.socialview

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import com.hendraanggrian.socialview.OnSocialClickListener
import com.hendraanggrian.socialview.SocialTextWatcher
import com.hendraanggrian.widget.SocialEditText

/**
 * @author Hendra Anggrian (com.hendraanggrian@gmail.com)
 */
class Example2Activity : BaseActivity(), OnSocialClickListener, SocialTextWatcher {

    @BindView(R.id.toolbar_example2) lateinit var toolbar: Toolbar
    @BindView(R.id.socialedittext_example2) lateinit var socialEditText: SocialEditText

    override val contentView: Int
        get() = R.layout.activity_example2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        socialEditText.setOnHashtagClickListener(this)
        socialEditText.setOnMentionClickListener(this)
        socialEditText.setHashtagTextChangedListener(this)
        socialEditText.setMentionTextChangedListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onSocialTextChanged(v: TextView, s: CharSequence) {
        Log.d("editing", s.toString())
    }

    override fun onSocialClick(v: TextView, text: String) {
        Toast.makeText(this, String.format("clicked:\n%s", text), Toast.LENGTH_SHORT).show()
    }
}