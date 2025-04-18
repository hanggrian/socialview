package com.example

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.hanggrian.socialview.autocomplete.Hashtag
import com.hanggrian.socialview.autocomplete.HashtagArrayAdapter
import com.hanggrian.socialview.autocomplete.Mention
import com.hanggrian.socialview.autocomplete.MentionArrayAdapter
import com.hanggrian.socialview.autocomplete.SocialAutoCompleteTextView

class ExampleActivity : AppCompatActivity() {
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
        defaultHashtagAdapter.addAll(Hashtags)

        defaultMentionAdapter = MentionArrayAdapter(this)
        defaultMentionAdapter.addAll(Mentions)

        customHashtagAdapter = PersonAdapter(this)
        customHashtagAdapter.addAll(Hashtags.map { Person(it.id) })

        customMentionAdapter = PersonAdapter(this)
        customMentionAdapter.addAll(Mentions.map { Person(it.username) })

        textView.hashtagAdapter = defaultHashtagAdapter
        textView.mentionAdapter = defaultMentionAdapter
        textView.setHashtagTextChangedListener { _, text -> Log.d("hashtag", text.toString()) }
        textView.setMentionTextChangedListener { _, text -> Log.d("mention", text.toString()) }
        textView.setOnHyperlinkClickListener { _, text -> Log.d("hyperlink", text.toString()) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_example, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.customAdapterItem -> {
                when {
                    !item.isChecked -> {
                        textView.hashtagAdapter = customHashtagAdapter
                        textView.mentionAdapter = customMentionAdapter
                    }
                    else -> {
                        textView.hashtagAdapter = defaultHashtagAdapter
                        textView.mentionAdapter = defaultMentionAdapter
                    }
                }
            }
            R.id.enableHashtagItem -> textView.isHashtagEnabled = !item.isChecked
            R.id.enableMentionItem -> textView.isMentionEnabled = !item.isChecked
            R.id.enableHyperlinkItem -> textView.isHyperlinkEnabled = !item.isChecked
            else -> return super.onOptionsItemSelected(item)
        }
        item.isChecked = !item.isChecked
        return true
    }
}
