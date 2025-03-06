package com.example

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hanggrian.appcompat.socialview.widget.SocialArrayAdapter

class PersonAdapter(context: Context) :
    SocialArrayAdapter<Person>(context, R.layout.item_person, R.id.textViewName) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: ViewHolder
        var view = convertView
        when (view) {
            null -> {
                view = LayoutInflater.from(context).inflate(R.layout.item_person, parent, false)
                holder = ViewHolder(view!!)
                view.tag = holder
            }

            else -> holder = view.tag as ViewHolder
        }
        getItem(position)?.let { model -> holder.textView.text = model.name }
        return view
    }

    private class ViewHolder(view: View) {
        val textView: TextView = view.findViewById(R.id.textViewName)
    }
}
