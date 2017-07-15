package com.hendraanggrian.socialview.commons

import android.content.Context
import android.net.Uri
import android.support.annotation.DrawableRes
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hendraanggrian.kota.content.toPx
import com.hendraanggrian.kota.view.findViewBy
import com.hendraanggrian.kota.view.setVisibleBy
import com.hendraanggrian.picasso.commons.target.Targets
import com.hendraanggrian.picasso.commons.transformation.Transformations
import com.squareup.picasso.RequestCreator
import com.squareup.picasso.getPicasso
import java.io.File

/**
 * Default adapter for displaying mention in [com.hendraanggrian.widget.SocialAutoCompleteTextView].

 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class MentionAdapter @JvmOverloads constructor(context: Context, @param:DrawableRes private val defaultAvatar: Int = R.drawable.ic_placeholder_mention) : SocialAdapter<Mention>(context, R.layout.widget_socialview_mention, R.id.textViewUsername) {

    private val filter = object : SocialFilter() {
        override fun convertResultToString(resultValue: Any) = (resultValue as Mention).username
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: ViewHolder
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.widget_socialview_mention, parent, false)
            holder = ViewHolder(convertView!!)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
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
            val progressBarSize = 24.toPx()
            request.error(defaultAvatar)
                    .transform(Transformations.circle())
                    .into(Targets.placeholder(holder.imageView, progressBarSize, progressBarSize))
            holder.textViewUsername.text = it.username
            if (holder.textViewDisplayname.setVisibleBy(!TextUtils.isEmpty(it.displayname))) {
                holder.textViewDisplayname.text = it.displayname
            }
        }
        return convertView
    }

    override fun getFilter() = filter

    private class ViewHolder(view: View) {
        val imageView = view.findViewBy<ImageView>(R.id.imageView)
        val textViewUsername = view.findViewBy<TextView>(R.id.textViewUsername)
        val textViewDisplayname = view.findViewBy<TextView>(R.id.textViewDisplayname)
    }
}