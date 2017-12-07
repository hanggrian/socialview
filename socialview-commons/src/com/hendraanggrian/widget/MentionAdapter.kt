package com.hendraanggrian.widget

import android.content.Context
import android.net.Uri
import android.support.annotation.DrawableRes
import android.text.TextUtils.isEmpty
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hendraanggrian.picasso.Targets
import com.hendraanggrian.picasso.Transformations
import com.hendraanggrian.picasso.picasso
import com.hendraanggrian.socialview.Mention
import com.hendraanggrian.socialview.commons.R
import com.squareup.picasso.RequestCreator
import java.io.File

/** Default adapter for displaying mention in [SocialAutoCompleteTextView]. */
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
            val request: RequestCreator = when {
                it.avatar == null -> context.picasso(defaultAvatar)
                it.avatar is Int -> context.picasso(it.avatar)
                it.avatar is String -> context.picasso(it.avatar)
                it.avatar is Uri -> context.picasso(it.avatar)
                it.avatar is File -> context.picasso(it.avatar)
                else -> throw IllegalStateException("Unsupported avatar type. See Mention.kt for more.")
            }
            request.error(defaultAvatar)
                    .transform(Transformations.circle())
                    .into(Targets.progress(holder.imageView, context.resources.getDimensionPixelSize(R.dimen.socialview_mention_progress)))
            holder.textViewUsername.text = it.username
            holder.textViewDisplayname.setVisibleThen(!isEmpty(it.displayname)) {
                holder.textViewDisplayname.text = it.displayname
            }
        }
        return _convertView
    }

    override fun getFilter() = filter

    private class ViewHolder(itemView: View) {
        val imageView = itemView.find<ImageView>(R.id.socialview_mention_avatar)
        val textViewUsername = itemView.find<TextView>(R.id.socialview_mention_username)
        val textViewDisplayname = itemView.find<TextView>(R.id.socialview_mention_displayname)
    }
}