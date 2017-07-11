package com.hendraanggrian.socialview.commons

import android.content.Context
import android.net.Uri
import android.support.annotation.DrawableRes
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.hendraanggrian.picasso.commons.target.Targets
import com.hendraanggrian.picasso.commons.transformation.Transformations
import com.hendraanggrian.support.utils.content.toPx
import com.hendraanggrian.support.utils.view.findViewBy
import com.hendraanggrian.support.utils.view.setVisibleBy
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import java.io.File

/**
 * Default adapter for displaying mention in [com.hendraanggrian.widget.SocialAutoCompleteTextView].

 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
class MentionAdapter @JvmOverloads constructor(context: Context, @param:DrawableRes private val defaultAvatar: Int = R.drawable.ic_placeholder_mention) : SocialAdapter<Mention>(context, R.layout.widget_socialview_mention, R.id.textViewUsername) {

    private val filter = object : SocialFilter() {
        override fun convertResultToString(resultValue: Any): CharSequence = (resultValue as Mention).username
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
        val item = getItem(position)
        if (item != null) {
            val picasso = Picasso.with(context)
            var request: RequestCreator? = null
            if (item.avatar == null) {
                request = picasso.load(defaultAvatar)
            } else if (item.avatar is Int) {
                request = picasso.load((item.avatar as Int?)!!)
            } else if (item.avatar is String) {
                request = picasso.load(item.avatar as String?)
            } else if (item.avatar is Uri) {
                request = picasso.load(item.avatar as Uri?)
            } else if (item.avatar is File) {
                request = picasso.load(item.avatar as File?)
            }

            if (request != null) {
                val progressBarSize = 24.toPx()
                val lp = FrameLayout.LayoutParams(progressBarSize, progressBarSize)
                lp.gravity = Gravity.CENTER
                val progressBar = ProgressBar(context)
                progressBar.layoutParams = lp
                request.error(defaultAvatar)
                        .transform(Transformations.circle())
                        .into(Targets.placeholder(holder.imageView, progressBar))
            }

            holder.textViewUsername.text = item.username
            if (holder.textViewDisplayname.setVisibleBy(!TextUtils.isEmpty(item.displayname))) {
                holder.textViewDisplayname.text = item.displayname
            }
        }
        return convertView
    }

    override fun getFilter(): Filter = filter

    private class ViewHolder(view: View) {
        val imageView: ImageView = view.findViewBy(R.id.imageView)
        val textViewUsername: TextView = view.findViewBy(R.id.textViewUsername)
        val textViewDisplayname: TextView = view.findViewBy(R.id.textViewDisplayname)
    }
}