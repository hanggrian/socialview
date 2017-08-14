package com.hendraanggrian.widget

import android.content.Context
import android.net.Uri
import android.support.annotation.DrawableRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hendraanggrian.common.content.getDimensionPixelSize
import com.hendraanggrian.common.text.isNotNullOrEmpty
import com.hendraanggrian.common.view.setVisibleThen
import com.hendraanggrian.picasso.picasso
import com.hendraanggrian.picasso.target.Targets
import com.hendraanggrian.picasso.transformation.Transformations
import com.hendraanggrian.socialview.Mention
import com.hendraanggrian.socialview.commons.R
import com.squareup.picasso.RequestCreator
import java.io.File

/**
 * Default adapter for displaying mention in [SocialAutoCompleteTextView].
 *
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class MentionAdapter @JvmOverloads constructor(
        context: Context,
        @DrawableRes private val defaultAvatar: Int = R.drawable.socialview_ic_mention_placeholder
) : FilteredAdapter<Mention>(context, R.layout.socialview_layout_mention, R.id.socialview_mention_username) {

    private val filter = object : SocialFilter() {
        override fun convertResultToString(resultValue: Any) = (resultValue as Mention).username
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: ViewHolder
        var _convertView = convertView
        if (_convertView == null) {
            _convertView = LayoutInflater.from(context).inflate(R.layout.socialview_layout_mention, parent, false)
            holder = ViewHolder(_convertView!!)
            _convertView.tag = holder
        } else {
            holder = _convertView.tag as ViewHolder
        }
        getItem(position)?.let {
            val request: RequestCreator
            if (it.avatar == null) {
                request = context.picasso(defaultAvatar)
            } else if (it.avatar is Int) {
                request = context.picasso(it.avatar)
            } else if (it.avatar is String) {
                request = context.picasso(it.avatar)
            } else if (it.avatar is Uri) {
                request = context.picasso(it.avatar)
            } else if (it.avatar is File) {
                request = context.picasso(it.avatar)
            } else {
                throw IllegalStateException("Unsupported avatar type. See Mention.kt for more.")
            }
            request.error(defaultAvatar)
                    .transform(Transformations.circle())
                    .into(Targets.progress(holder.imageView, context.getDimensionPixelSize(R.dimen.socialview_mention_progress)))
            holder.textViewUsername.text = it.username
            holder.textViewDisplayname.setVisibleThen(it.displayname.isNotNullOrEmpty()) {
                holder.textViewDisplayname.text = it.displayname
            }
        }
        return _convertView
    }

    override fun getFilter() = filter

    private class ViewHolder(itemView: View) {
        val imageView = itemView.findViewById<ImageView>(R.id.socialview_mention_avatar)!!
        val textViewUsername = itemView.findViewById<TextView>(R.id.socialview_mention_username)!!
        val textViewDisplayname = itemView.findViewById<TextView>(R.id.socialview_mention_displayname)!!
    }
}