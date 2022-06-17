package com.hendraanggrian.appcompat.socialview.commons

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hendraanggrian.appcompat.socialview.commons.test.R
import com.hendraanggrian.appcompat.widget.SocialArrayAdapter

class PersonAdapter(context: Context) : SocialArrayAdapter<Person>(context, R.layout.item_person, R.id.textViewName) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val holder: ViewHolder
        when (view) {
            null -> {
                view = LayoutInflater.from(context).inflate(R.layout.item_person, parent, false)
                holder = ViewHolder(view!!)
                view.tag = holder
            }
            else -> holder = view.tag as ViewHolder
        }
        getItem(position)?.let { person -> holder.textView.text = person.name }
        return view
    }

    private class ViewHolder(itemView: View) {
        val textView: TextView = itemView.findViewById<View>(R.id.textViewName) as TextView
    }
}
