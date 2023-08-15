package com.example.socialview

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.hendraanggrian.appcompat.socialview.Hashtag
import com.hendraanggrian.appcompat.socialview.Mention
import com.hendraanggrian.appcompat.widget.HashtagArrayAdapter
import com.hendraanggrian.appcompat.widget.MentionArrayAdapter
import com.hendraanggrian.appcompat.widget.SocialArrayAdapter
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView

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
                "https://avatars1.githubusercontent.com/u/11507430?s=460&v=4"
            )
        )

        customHashtagAdapter = PersonAdapter(this)
        customHashtagAdapter.addAll(
            Person(HASHTAG1),
            Person(HASHTAG2),
            Person(HASHTAG3)
        )

        customMentionAdapter = PersonAdapter(this)
        customMentionAdapter.addAll(
            Person(MENTION1_USERNAME),
            Person(MENTION2_USERNAME),
            Person(MENTION3_USERNAME)
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

    data class Person(val name: String)

    class PersonAdapter(context: Context) :
        SocialArrayAdapter<Person>(context, R.layout.item_person, R.id.textViewName) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val holder: ViewHolder
            var view = convertView
            when (view) {
                null -> {
                    view = LayoutInflater.from(context).inflate(R.layout.item_person, parent, false)
                    holder = ViewHolder(view!!)
                    view.tag = holder
                }

                else -> holder = view.tag as ViewHolder
            }
            getItem(position)?.let { model -> holder.textView.text = model.name }
            return view
        }

        private class ViewHolder(view: View) {
            val textView: TextView = view.findViewById(R.id.textViewName)
        }
    }
}
