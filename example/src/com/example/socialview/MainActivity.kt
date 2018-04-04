package com.example.socialview

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log.d
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.hendraanggrian.widget.Hashtag
import com.hendraanggrian.widget.HashtagAdapter
import com.hendraanggrian.widget.Mention
import com.hendraanggrian.widget.MentionAdapter
import com.hendraanggrian.widget.SocialAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var defaultHashtagAdapter: ArrayAdapter<Hashtag>
    private lateinit var defaultMentionAdapter: ArrayAdapter<Mention>
    private lateinit var customHashtagAdapter: ArrayAdapter<Person>
    private lateinit var customMentionAdapter: ArrayAdapter<Person>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        defaultHashtagAdapter = HashtagAdapter(this)
        defaultHashtagAdapter.addAll(
            Hashtag(getString(R.string.hashtag1)),
            Hashtag(getString(R.string.hashtag2), resources.getInteger(R.integer.hashtag2)),
            Hashtag(getString(R.string.hashtag3), resources.getInteger(R.integer.hashtag3)))

        defaultMentionAdapter = MentionAdapter(this)
        defaultMentionAdapter.addAll(
            Mention(getString(R.string.mention1_username)),
            Mention(getString(R.string.mention2_username), getString(R.string.mention2_displayname), R.mipmap.ic_launcher),
            Mention(getString(R.string.mention3_username), getString(R.string.mention3_displayname), "https://avatars0.githubusercontent.com/u/11507430?v=3&s=460"))

        customHashtagAdapter = PersonAdapter(this)
        customHashtagAdapter.addAll(
            Person(getString(R.string.hashtag1)),
            Person(getString(R.string.hashtag2)),
            Person(getString(R.string.hashtag3)))

        customMentionAdapter = PersonAdapter(this)
        customMentionAdapter.addAll(
            Person(getString(R.string.mention1_username)),
            Person(getString(R.string.mention2_username)),
            Person(getString(R.string.mention3_username)))

        textView.threshold = 1
        textView.hashtagAdapter = defaultHashtagAdapter
        textView.mentionAdapter = defaultMentionAdapter
        textView.setHashtagTextChangedListener { _, s -> d("hashtag", s) }
        textView.setMentionTextChangedListener { _, s -> d("mention", s) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
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
            R.id.enableHashtagItem -> textView.setHashtagEnabled(item.isChecked)
            R.id.enableMentionItem -> textView.setMentionEnabled(item.isChecked)
            R.id.enableHyperlinkItem -> textView.setHyperlinkEnabled(item.isChecked)
        }
        return super.onOptionsItemSelected(item)
    }

    data class Person(val name: String)

    class PersonAdapter(context: Context) : SocialAdapter<Person>(context, R.layout.item_person, R.id.textViewName) {

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