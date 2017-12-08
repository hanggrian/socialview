package com.hendraanggrian.socialview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hendraanggrian.socialview.commons.test.R
import com.hendraanggrian.widget.SocialAdapter

class PersonAdapter(context: Context) : SocialAdapter<Person>(context, R.layout.item_person, R.id.textViewName) {

    override fun Person.convertToString(): String = name

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var _convertView = convertView
        val holder: ViewHolder
        if (_convertView == null) {
            _convertView = LayoutInflater.from(context).inflate(R.layout.item_person, parent, false)
            holder = ViewHolder(_convertView!!)
            _convertView.tag = holder
        } else {
            holder = _convertView.tag as ViewHolder
        }
        getItem(position)?.let { person ->
            holder.textView.text = person.name
        }
        return _convertView
    }

    private class ViewHolder(itemView: View) {
        val textView: TextView = itemView.findViewById<View>(R.id.textViewName) as TextView
    }
}