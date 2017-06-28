package com.example.socialview

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import com.hendraanggrian.socialview.SociableView
import com.hendraanggrian.socialview.SocialTextWatcher
import com.hendraanggrian.socialview.commons.SocialAdapter
import com.hendraanggrian.support.utils.util.Logs
import kotlinx.android.synthetic.main.activity_example3.*

/**
 * @author Hendra Anggrian (com.hendraanggrian@gmail.com)
 */
class Example4Activity : AppCompatActivity(), SocialTextWatcher {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example3)
        setSupportActionBar(toolbar)

        textView.threshold = 1
        textView.hashtagAdapter = CustomAdapter(this)
        textView.mentionAdapter = CustomAdapter(this)
        textView.setHashtagTextChangedListener(this)
        textView.setHashtagTextChangedListener(this)

        val hashtag1 = Model("follow")
        val hashtag2 = Model("followme")
        val hashtag3 = Model("followmeorillkillyou")
        textView.hashtagAdapter.addAll(hashtag1, hashtag2, hashtag3)

        val mention1 = Model("dirtyhobo")
        val mention2 = Model("hobo")
        val mention3 = Model("hendraanggrian")
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

    class Model constructor(val content: String)

    private class CustomAdapter constructor(context: Context) : SocialAdapter<Model>(context, R.layout.item_custom, R.id.textview_custom) {
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
                filter = object : SuggestionFilter() {
                    override fun getString(item: Model): String {
                        return item.content
                    }
                }
            return filter as Filter
        }

        private class ViewHolder constructor(view: View) {
            val textView: TextView = view.findViewById(R.id.textview_custom) as TextView
        }
    }
}