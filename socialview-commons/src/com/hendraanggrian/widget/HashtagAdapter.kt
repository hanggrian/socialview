package com.hendraanggrian.widget

import android.content.Context
import android.support.annotation.PluralsRes
import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hendraanggrian.socialview.Hashtag
import com.hendraanggrian.socialview.commons.R

/**
 * Default adapter for displaying hashtag in [SocialAutoCompleteTextView].
 * Note that this adapter is completely optional, any adapter extending
 * [android.widget.ArrayAdapter] can be attached to [SocialAutoCompleteTextView].
 */
class HashtagAdapter @JvmOverloads constructor(
    context: Context,
    @PluralsRes private val countPlural: Int = R.plurals.posts
) : SocialAdapter<Hashtag>(
    context,
    R.layout.socialview_layout_hashtag,
    R.id.socialview_hashtag
) {

    override fun Hashtag.convertToString(): String = hashtag

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: ViewHolder
        var _convertView = convertView
        if (_convertView == null) {
            _convertView = from(context).inflate(R.layout.socialview_layout_hashtag, parent, false)
            holder = ViewHolder(_convertView!!)
            _convertView.tag = holder
        } else {
            holder = _convertView.tag as ViewHolder
        }
        getItem(position)?.let { hashtag ->
            holder.hashtagView.text = hashtag.hashtag
            hashtag.count?.let {
                holder.countView.text = context.resources.getQuantityString(countPlural, it)
            }
        }
        return _convertView
    }

    private class ViewHolder(itemView: View) {
        val hashtagView = itemView.findViewById<TextView>(R.id.socialview_hashtag)
        val countView = itemView.findViewById<TextView>(R.id.socialview_hashtag_count)
    }
}