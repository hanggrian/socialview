package com.hendraanggrian.appcompat.widget

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater.from
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.hendraanggrian.appcompat.socialview.Mentionable
import com.hendraanggrian.appcompat.socialview.commons.R
import com.hendraanggrian.pikasso.into
import com.hendraanggrian.pikasso.picasso
import com.hendraanggrian.pikasso.transformations.circle
import java.io.File

/**
 * Default adapter for displaying mention in [SocialAutoCompleteTextView].
 * Note that this adapter is completely optional, any adapter extending
 * [android.widget.ArrayAdapter] can be attached to [SocialAutoCompleteTextView].
 */
class MentionArrayAdapter<T : Mentionable> @JvmOverloads constructor(
    context: Context,
    @DrawableRes private val defaultAvatar: Int = R.drawable.socialview_ic_mention_placeholder
) : SuggestionArrayAdapter<T>(
    context,
    R.layout.socialview_layout_mention,
    R.id.socialview_mention_username
) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: ViewHolder
        var view = convertView
        when (view) {
            null -> {
                view = from(context).inflate(R.layout.socialview_layout_mention, parent, false)
                holder = ViewHolder(view!!)
                view.tag = holder
            }
            else -> holder = view.tag as ViewHolder
        }
        getItem(position)?.let { mention ->
            when (mention.avatar) {
                null -> picasso.load(defaultAvatar)
                is Int -> picasso.load(mention.avatar as Int)
                is String -> picasso.load(mention.avatar as String)
                is Uri -> picasso.load(mention.avatar as Uri)
                is File -> picasso.load(mention.avatar as File)
                else -> error("Unsupported avatar type")
            }.error(defaultAvatar)
                .circle()
                .into(holder.avatarView) {
                    onSuccess {
                        holder.loadingView.visibility = GONE
                    }
                    onError {
                        holder.loadingView.visibility = GONE
                    }
                }
            holder.usernameView.text = mention.username
            if (!mention.displayname.isNullOrEmpty()) {
                holder.displaynameView.text = mention.displayname
            }
        }
        return view
    }

    private class ViewHolder(itemView: View) {
        val avatarView: ImageView = itemView.findViewById(R.id.socialview_mention_avatar)
        val loadingView: ProgressBar = itemView.findViewById(R.id.socialview_mention_loading)
        val usernameView: TextView = itemView.findViewById(R.id.socialview_mention_username)
        val displaynameView: TextView = itemView.findViewById(R.id.socialview_mention_displayname)
    }
}