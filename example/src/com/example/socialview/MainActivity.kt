package com.example.socialview

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.hendraanggrian.socialview.SociableView
import com.hendraanggrian.socialview.SocialTextWatcher
import com.hendraanggrian.socialview.commons.*
import com.hendraanggrian.support.utils.util.Logs
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author Hendra Anggrian (com.hendraanggrian@gmail.com)
 */
class MainActivity : AppCompatActivity(), SocialTextWatcher {

    var defaultHashtagAdapter: ArrayAdapter<Hashtag>? = null
    var defaultMentionAdapter: ArrayAdapter<Mention>? = null
    var customHashtagAdapter: ArrayAdapter<Model>? = null
    var customMentionAdapter: ArrayAdapter<Model>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        defaultHashtagAdapter = HashtagAdapter(this)
        defaultHashtagAdapter!!.addAll(
                Hashtag("follow"),
                Hashtag("followme", 1000),
                Hashtag("followmeorillkillyou", 500))

        defaultMentionAdapter = MentionAdapter(this)
        defaultMentionAdapter!!.addAll(
                Mention("dirtyhobo"),
                Mention("hobo", "Regular Hobo", R.mipmap.ic_launcher),
                Mention("hendraanggrian", "Hendra Anggrian", "https://avatars0.githubusercontent.com/u/11507430?v=3&s=460")
        )

        customHashtagAdapter = CustomAdapter(this)
        customHashtagAdapter!!.addAll(
                Model("follow"),
                Model("followme"),
                Model("followmeorillkillyou")
        )

        customMentionAdapter = CustomAdapter(this)
        customMentionAdapter!!.addAll(
                Model("dirtyhobo"),
                Model("hobo"),
                Model("hendraanggrian"))

        textView.threshold = 1
        textView.hashtagAdapter = defaultHashtagAdapter
        textView.mentionAdapter = defaultMentionAdapter
        textView.setHashtagTextChangedListener(this)
        textView.setMentionTextChangedListener(this)
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

    override fun onSocialTextChanged(v: SociableView, s: CharSequence) {
        Logs.d("editing", s)
    }

    class Model constructor(val content: String)

    class CustomAdapter constructor(context: Context) : SocialAdapter<Model>(context, R.layout.item_custom, R.id.textview_custom) {
        private var filter: Filter? = null

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            val holder: ViewHolder
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_custom, parent, false)
                holder = ViewHolder(convertView!!)
                convertView.tag = holder
            } else {
                holder = convertView.tag as ViewHolder
            }
            val model = getItem(position)
            if (model != null) {
                holder.textView.text = model.content
            }
            return convertView
        }

        override fun getFilter(): Filter {
            if (filter == null)
                filter = object : SocialFilter() {
                    override fun convertResultToString(resultValue: Any): CharSequence {
                        return (resultValue as Model).content
                    }
                }
            return filter as Filter
        }

        private class ViewHolder constructor(view: View) {
            val textView: TextView = view.findViewById(R.id.textview_custom) as TextView
        }
    }
}