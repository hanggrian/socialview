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
import com.hendraanggrian.socialview.SocialView
import com.hendraanggrian.widget.SocialEditText

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class Example2Activity : BaseActivity(), OnSocialClickListener, SocialTextWatcher {

    @BindView(R.id.toolbar_example2) lateinit var toolbar: Toolbar
    @BindView(R.id.socialedittext_example2) lateinit var socialEditText: SocialEditText

    override val contentView: Int
        get() = R.layout.activity_example2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        socialEditText.setOnSocialClickListener(this)
        socialEditText.setSocialTextChangedListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onSocialTextChanged(v: TextView, @SocialView.Type type: Int, s: CharSequence) {
        Log.d("editing", String.format("%s - %s", type, s))
    }

    override fun onSocialClick(v: TextView, @SocialView.Type type: Int, text: CharSequence) {
        Toast.makeText(this, String.format("%s clicked:\n%s", type, text), Toast.LENGTH_SHORT).show()
    }
}