package com.hendraanggrian.socialview.commons

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hendraanggrian.support.utils.view.findViewBy
import com.hendraanggrian.support.utils.view.setVisibleBy
import java.text.NumberFormat
import java.util.*

/**
 * Default adapter for displaying hashtag in [com.hendraanggrian.widget.SocialAutoCompleteTextView].

 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class HashtagAdapter(context: Context) : SocialAdapter<Hashtag>(context, R.layout.widget_socialview_hashtag, R.id.textViewHashtag) {

    private val filter = object : SocialFilter() {
        override fun convertResultToString(resultValue: Any) = (resultValue as Hashtag).hashtag
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: ViewHolder
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.widget_socialview_hashtag, parent, false)
            holder = ViewHolder(convertView!!)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        getItem(position)?.let {
            holder.textViewHashtag.text = it.hashtag
            if (holder.textViewCount.setVisibleBy(it.count != null)) {
                holder.textViewCount.text = if (it.count!! < 2)
                    it.count.toString() + " post"
                else
                    NumberFormat.getNumberInstance(Locale.US).format(it.count.toLong()) + " posts"
            }
        }
        return convertView
    }

    override fun getFilter() = filter

    private class ViewHolder(view: View) {
        val textViewHashtag: TextView = view.findViewBy(R.id.textViewHashtag)
        val textViewCount: TextView = view.findViewBy(R.id.textViewCount)
    }
}