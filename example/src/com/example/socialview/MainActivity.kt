package com.example.socialview

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.hendraanggrian.socialview.Hashtag
import com.hendraanggrian.socialview.Mention
import com.hendraanggrian.widget.HashtagAdapter
import com.hendraanggrian.widget.MentionAdapter
import com.hendraanggrian.widget.SocialAdapter
import kota.debug
import kota.find
import kota.inflateMenu
import kota.resources.getInt
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
            Hashtag(getString(R.string.hashtag2), getInt(R.integer.hashtag2)),
            Hashtag(getString(R.string.hashtag3), getInt(R.integer.hashtag3)))

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
        textView.setHashtagTextChangedListener { _, s -> debug("hashtag", s) }
        textView.setMentionTextChangedListener { _, s -> debug("mention", s) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        inflateMenu(R.menu.activity_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.title) {
            "Default" -> {
                textView.hashtagAdapter = customHashtagAdapter
                textView.mentionAdapter = customMentionAdapter
                item.title = "Custom"
            }
            "Custom" -> {
                textView.hashtagAdapter = defaultHashtagAdapter
                textView.mentionAdapter = defaultMentionAdapter
                item.title = "Default"
            }
        }
        return super.onOptionsItemSelected(item)
    }

    data class Person(val name: String)

    class PersonAdapter(context: Context) : SocialAdapter<Person>(context, R.layout.item_person, R.id.textViewName) {

        override fun Person.convertToString(): String = name

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val holder: ViewHolder
            var _convertView = convertView
            if (_convertView == null) {
                _convertView = LayoutInflater.from(context).inflate(R.layout.item_person, parent, false)
                holder = ViewHolder(_convertView!!)
                _convertView.tag = holder
            } else {
                holder = _convertView.tag as ViewHolder
            }
            getItem(position)?.let { model ->
                holder.textView.text = model.name
            }
            return _convertView
        }

        private class ViewHolder(view: View) {
            val textView = view.find<TextView>(R.id.textViewName)
        }
    }
}