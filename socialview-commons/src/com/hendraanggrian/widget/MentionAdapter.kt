package com.hendraanggrian.widget

import android.content.Context
import android.net.Uri
import android.support.annotation.DrawableRes
import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hendraanggrian.pikasso.circle
import com.hendraanggrian.pikasso.picasso
import com.hendraanggrian.pikasso.toProgressTarget
import com.hendraanggrian.socialview.Mention
import com.hendraanggrian.socialview.commons.R
import java.io.File

/**
 * Default adapter for displaying mention in [SocialAutoCompleteTextView].
 * Note that this adapter is completely optional, any adapter extending
 * [android.widget.ArrayAdapter] can be attached to [SocialAutoCompleteTextView].
 */
class MentionAdapter @JvmOverloads constructor(
    context: Context,
    @DrawableRes private val defaultAvatar: Int = R.drawable.socialview_ic_mention_placeholder
) : SocialAdapter<Mention>(
    context,
    R.layout.socialview_layout_mention,
    R.id.socialview_mention_username
) {

    override fun Mention.convertToString(): String = username

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: ViewHolder
        var _convertView = convertView
        when (_convertView) {
            null -> {
                _convertView = from(context)
                    .inflate(R.layout.socialview_layout_mention, parent, false)
                holder = ViewHolder(_convertView!!)
                _convertView.tag = holder
            }
            else -> holder = _convertView.tag as ViewHolder
        }
        getItem(position)?.let { mention ->
            when (mention.avatar) {
                null -> picasso.load(defaultAvatar)
                is Int -> picasso.load(mention.avatar)
                is String -> picasso.load(mention.avatar)
                is Uri -> picasso.load(mention.avatar)
                is File -> picasso.load(mention.avatar)
                else -> error("Unsupported avatar type. See Mention.kt for more.")
            }.error(defaultAvatar)
                .circle()
                .into(holder.avatarView.toProgressTarget(
                    context.resources.getDimensionPixelSize(
                        R.dimen.socialview_mention_progress_bar)))
            holder.usernameView.text = mention.username
            if (!mention.displayname.isNullOrEmpty()) {
                holder.displaynameView.text = mention.displayname
            }
        }
        return _convertView
    }

    private class ViewHolder(itemView: View) {
        val avatarView: ImageView = itemView.findViewById(R.id.socialview_mention_avatar)
        val usernameView: TextView = itemView.findViewById(R.id.socialview_mention_username)
        val displaynameView: TextView = itemView.findViewById(R.id.socialview_mention_displayname)
    }
}