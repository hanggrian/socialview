package com.example.socialview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.hendraanggrian.socialview.SociableView
import com.hendraanggrian.socialview.SocialTextWatcher
import com.hendraanggrian.socialview.commons.Hashtag
import com.hendraanggrian.socialview.commons.HashtagAdapter
import com.hendraanggrian.socialview.commons.Mention
import com.hendraanggrian.socialview.commons.MentionAdapter
import com.hendraanggrian.support.utils.util.Logs
import kotlinx.android.synthetic.main.activity_example3.*

/**
 * @author Hendra Anggrian (com.hendraanggrian@gmail.com)
 */
class Example3Activity : AppCompatActivity(), SocialTextWatcher {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example3)
        setSupportActionBar(toolbar)

        textView.threshold = 1
        textView.hashtagAdapter = HashtagAdapter(this)
        textView.mentionAdapter = MentionAdapter(this)
        textView.setHashtagTextChangedListener(this)
        textView.setMentionTextChangedListener(this)

        val hashtag1 = Hashtag("follow")
        val hashtag2 = Hashtag.Builder("followme")
                .setCount(1000)
                .build()
        val hashtag3 = Hashtag.Builder("followmeorillkillyou")
                .setCount(500)
                .build()
        textView.hashtagAdapter.addAll(hashtag1, hashtag2, hashtag3)

        val mention1 = Mention("dirtyhobo")
        val mention2 = Mention.Builder("hobo")
                .setDisplayname("Regular Hobo")
                .setAvatarDrawable(R.mipmap.ic_launcher)
                .build()
        val mention3 = Mention.Builder("hendraanggrian")
                .setDisplayname("Hendra Anggrian")
                .setAvatarURL("https://avatars0.githubusercontent.com/u/11507430?v=3&s=460")
                .build()
        textView.mentionAdapter.addAll(mention1, mention2, mention3)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onSocialTextChanged(v: SociableView, s: CharSequence) {
        Logs.d("editing", s)
    }
}