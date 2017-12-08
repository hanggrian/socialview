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
import com.hendraanggrian.socialview.find
import com.hendraanggrian.socialview.setVisibleThen
import java.io.File

/**
 * Default adapter for displaying mention in [SocialAutoCompleteTextView].
 * Note that this adapter is completely optional, any adapter extending [android.widget.ArrayAdapter]
 * can be attached to [SocialAutoCompleteTextView].
 */
class MentionAdapter @JvmOverloads constructor(
        context: Context,
        @DrawableRes private val defaultAvatar: Int = R.drawable.socialview_ic_mention_placeholder
) : SocialAdapter<Mention>(context, R.layout.socialview_layout_mention, R.id.socialview_mention_username) {

    override fun Mention.convertToString(): String = username

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
        getItem(position)?.let { mention ->
            when (mention.avatar) {
                null -> context.picasso(defaultAvatar)
                is Int -> context.picasso(mention.avatar)
                is String -> context.picasso(mention.avatar)
                is Uri -> context.picasso(mention.avatar)
                is File -> context.picasso(mention.avatar)
                else -> throw IllegalStateException("Unsupported avatar type. See Mention.kt for more.")
            }.error(defaultAvatar)
                    .transform(Transformations.circle())
                    .into(Targets.progress(holder.imageView, context.resources.getDimensionPixelSize(R.dimen.socialview_mention_progress)))
            holder.textViewUsername.text = mention.username
            holder.textViewDisplayname.setVisibleThen(!isEmpty(mention.displayname)) {
                holder.textViewDisplayname.text = mention.displayname
            }
        }
        return _convertView
    }

    private class ViewHolder(itemView: View) {
        val imageView = itemView.find<ImageView>(R.id.socialview_mention_avatar)
        val textViewUsername = itemView.find<TextView>(R.id.socialview_mention_username)
        val textViewDisplayname = itemView.find<TextView>(R.id.socialview_mention_displayname)
    }
}