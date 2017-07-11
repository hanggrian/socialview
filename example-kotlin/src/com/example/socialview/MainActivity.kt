package com.example.socialview

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.hendraanggrian.socialview.commons.*
import com.hendraanggrian.support.utils.content.int
import com.hendraanggrian.support.utils.content.string
import com.hendraanggrian.support.utils.view.findViewBy
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author Hendra Anggrian (com.hendraanggrian@gmail.com)
 */
class MainActivity : AppCompatActivity() {

    var defaultHashtagAdapter: ArrayAdapter<Hashtag>? = null
    var defaultMentionAdapter: ArrayAdapter<Mention>? = null
    var customHashtagAdapter: ArrayAdapter<Person>? = null
    var customMentionAdapter: ArrayAdapter<Person>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        defaultHashtagAdapter = HashtagAdapter(this)
        defaultHashtagAdapter!!.addAll(
                Hashtag(R.string.hashtag1.string(this)),
                Hashtag(R.string.hashtag2.string(this), R.integer.hashtag2.int(this)),
                Hashtag(R.string.hashtag3.string(this), R.integer.hashtag3.int(this)))

        defaultMentionAdapter = MentionAdapter(this)
        defaultMentionAdapter!!.addAll(
                Mention(R.string.mention1_username.string(this)),
                Mention(R.string.mention2_username.string(this), R.string.mention2_displayname.string(this), R.mipmap.ic_launcher),
                Mention(R.string.mention3_username.string(this), R.string.mention3_displayname.string(this), "https://avatars0.githubusercontent.com/u/11507430?v=3&s=460"))

        customHashtagAdapter = PersonAdapter(this)
        customHashtagAdapter!!.addAll(
                Person(R.string.hashtag1.string(this)),
                Person(R.string.hashtag2.string(this)),
                Person(R.string.hashtag3.string(this)))

        customMentionAdapter = PersonAdapter(this)
        customMentionAdapter!!.addAll(
                Person(R.string.mention1_username.string(this)),
                Person(R.string.mention2_username.string(this)),
                Person(R.string.mention3_username.string(this)))

        textView.threshold = 1
        textView.hashtagAdapter = defaultHashtagAdapter
        textView.mentionAdapter = defaultMentionAdapter
        textView.setHashtagTextChangedListener { _, s -> Log.d("editing", s.toString()) }
        textView.setMentionTextChangedListener { _, s -> Log.d("editing", s.toString()) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
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

    data class Person constructor(val name: String)

    class PersonAdapter constructor(context: Context) : SocialAdapter<Person>(context, R.layout.item_person, R.id.textViewName) {
        private var filter: Filter? = null

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            val holder: ViewHolder
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_person, parent, false)
                holder = ViewHolder(convertView)
                convertView.tag = holder
            } else {
                holder = convertView.tag as ViewHolder
            }
            val model = getItem(position)
            if (model != null) {
                holder.textView.text = model.name
            }
            return convertView!!
        }

        override fun getFilter(): Filter {
            if (filter == null)
                filter = object : SocialFilter() {
                    override fun convertResultToString(resultValue: Any): CharSequence {
                        return (resultValue as Person).name
                    }
                }
            return filter as Filter
        }

        private class ViewHolder constructor(view: View) {
            val textView = view.findViewBy<TextView>(R.id.textViewName)
        }
    }
}