package com.hendraanggrian.appcompat.widget

import android.content.Context
import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.PluralsRes
import com.hendraanggrian.appcompat.socialview.Hashtagable
import com.hendraanggrian.appcompat.socialview.commons.R

/**
 * Default adapter for displaying hashtag in [SocialAutoCompleteTextView].
 * Note that this adapter is completely optional, any adapter extending
 * [android.widget.ArrayAdapter] can be attached to [SocialAutoCompleteTextView].
 */
class HashtagArrayAdapter<T : Hashtagable> @JvmOverloads constructor(
    context: Context,
    @PluralsRes private val countPlural: Int = R.plurals.posts
) : SocialArrayAdapter<T>(
    context,
    R.layout.socialview_layout_hashtag,
    R.id.socialview_hashtag
) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: ViewHolder
        var view = convertView
        when (view) {
            null -> {
                view = from(context).inflate(R.layout.socialview_layout_hashtag, parent, false)
                holder = ViewHolder(view!!)
                view.tag = holder
            }
            else -> holder = view.tag as ViewHolder
        }
        getItem(position)?.let { hashtag ->
            holder.hashtagView.text = hashtag.hashtag
            if (hashtag.count != null) {
                holder.countView.visibility = View.VISIBLE
                holder.countView.text =
                    context.resources.getQuantityString(countPlural, hashtag.count!!, hashtag.count)
            } else {
                holder.countView.visibility = View.GONE
            }
        }
        return view
    }

    private class ViewHolder(itemView: View) {
        val hashtagView: TextView = itemView.findViewById(R.id.socialview_hashtag)
        val countView: TextView = itemView.findViewById(R.id.socialview_hashtag_count)
    }
}
