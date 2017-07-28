package com.hendraanggrian.widget

import android.content.Context
import android.net.Uri
import android.support.annotation.DrawableRes
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hendraanggrian.kota.content.res.getDimensionPixelSize
import com.hendraanggrian.kota.view.setVisibleBy
import com.hendraanggrian.picasso.target.Targets
import com.hendraanggrian.picasso.transformation.Transformations
import com.hendraanggrian.socialview.commons.Mention
import com.hendraanggrian.socialview.commons.R
import com.squareup.picasso.RequestCreator
import com.squareup.picasso.getPicasso
import java.io.File

/**
 * Default adapter for displaying mention in [SocialAutoCompleteTextView].
 *
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class MentionAdapter @JvmOverloads constructor(context: Context, @DrawableRes private val defaultAvatar: Int = R.drawable.socialview_ic_mention_placeholder) :
        FilteredAdapter<Mention>(context, R.layout.socialview_layout_mention, R.id.socialview_mention_username) {

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
                request = context.getPicasso().load(defaultAvatar)
            } else if (it.avatar is Int) {
                request = context.getPicasso().load(it.avatar)
            } else if (it.avatar is String) {
                request = context.getPicasso().load(it.avatar)
            } else if (it.avatar is Uri) {
                request = context.getPicasso().load(it.avatar)
            } else if (it.avatar is File) {
                request = context.getPicasso().load(it.avatar)
            } else {
                throw IllegalStateException("Unsupported avatar type. See Mention.kt for more.")
            }
            val progressBarSize = context.getDimensionPixelSize(R.dimen.socialview_mention_progress)
            request.error(defaultAvatar)
                    .transform(Transformations.circle())
                    .into(Targets.placeholder(holder.imageView, progressBarSize, progressBarSize))
            holder.textViewUsername.text = it.username
            if (holder.textViewDisplayname.setVisibleBy(!TextUtils.isEmpty(it.displayname))) {
                holder.textViewDisplayname.text = it.displayname
            }
        }
        return _convertView
    }

    override fun getFilter() = filter

    private class ViewHolder(view: View) {
        val imageView = view.findViewById<ImageView>(R.id.socialview_mention_avatar)!!
        val textViewUsername = view.findViewById<TextView>(R.id.socialview_mention_username)!!
        val textViewDisplayname = view.findViewById<TextView>(R.id.socialview_mention_displayname)!!
    }
}