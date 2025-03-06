package com.example

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.hanggrian.appcompat.socialview.autocomplete.Hashtag
import com.hanggrian.appcompat.socialview.autocomplete.Mention
import com.hanggrian.appcompat.socialview.widget.HashtagArrayAdapter
import com.hanggrian.appcompat.socialview.widget.MentionArrayAdapter
import com.hanggrian.appcompat.socialview.widget.SocialAutoCompleteTextView

class ExampleActivity : AppCompatActivity() {
    private companion object {
        const val HASHTAG1 = "follow"
        const val HASHTAG2 = "followme"
        const val HASHTAG3 = "followmeorillkillyou"
        const val HASHTAG2_COUNT = 1000
        const val HASHTAG3_COUNT = 500
        const val MENTION1_USERNAME = "dirtyhobo"
        const val MENTION2_USERNAME = "hobo"
        const val MENTION3_USERNAME = "hendraanggrian"
        const val MENTION2_DISPLAYNAME = "Regular Hobo"
        const val MENTION3_DISPLAYNAME = "Hendra Anggrian"
    }

    private lateinit var toolbar: Toolbar
    private lateinit var textView: SocialAutoCompleteTextView

    private lateinit var defaultHashtagAdapter: ArrayAdapter<Hashtag>
    private lateinit var defaultMentionAdapter: ArrayAdapter<Mention>
    private lateinit var customHashtagAdapter: ArrayAdapter<Person>
    private lateinit var customMentionAdapter: ArrayAdapter<Person>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)
        toolbar = findViewById(R.id.toolbar)
        textView = findViewById(R.id.textView)
        setSupportActionBar(toolbar)

        defaultHashtagAdapter = HashtagArrayAdapter(this)
        defaultHashtagAdapter.addAll(
            Hashtag(HASHTAG1),
            Hashtag(HASHTAG2, HASHTAG2_COUNT),
            Hashtag(HASHTAG3, HASHTAG3_COUNT)
        )

        defaultMentionAdapter = MentionArrayAdapter(this)
        defaultMentionAdapter.addAll(
            Mention(MENTION1_USERNAME),
            Mention(MENTION2_USERNAME, MENTION2_DISPLAYNAME),
            Mention(
                MENTION3_USERNAME,
                MENTION3_DISPLAYNAME,
                "https://avatars1.githubusercontent.com/u/11507430?s=460&v=4",
            )
        )

        customHashtagAdapter = PersonAdapter(this)
        customHashtagAdapter.addAll(
            Person(HASHTAG1),
            Person(HASHTAG2),
            Person(HASHTAG3),
        )

        customMentionAdapter = PersonAdapter(this)
        customMentionAdapter.addAll(
            Person(MENTION1_USERNAME),
            Person(MENTION2_USERNAME),
            Person(MENTION3_USERNAME),
        )

        textView.hashtagAdapter = defaultHashtagAdapter
        textView.mentionAdapter = defaultMentionAdapter
        textView.setHashtagTextChangedListener { _, text -> Log.d("hashtag", text.toString()) }
        textView.setMentionTextChangedListener { _, text -> Log.d("mention", text.toString()) }
        textView.setOnHyperlinkClickListener { _, text -> Log.d("hyperlink", text.toString()) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_demo, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.isChecked = !item.isChecked
        when (item.itemId) {
            R.id.customAdapterItem -> when (item.isChecked) {
                false -> {
                    textView.hashtagAdapter = customHashtagAdapter
                    textView.mentionAdapter = customMentionAdapter
                }

                else -> {
                    textView.hashtagAdapter = defaultHashtagAdapter
                    textView.mentionAdapter = defaultMentionAdapter
                }
            }

            R.id.enableHashtagItem -> textView.isHashtagEnabled = item.isChecked
            R.id.enableMentionItem -> textView.isMentionEnabled = item.isChecked
            R.id.enableHyperlinkItem -> textView.isHyperlinkEnabled = item.isChecked
        }
        return super.onOptionsItemSelected(item)
    }
}
