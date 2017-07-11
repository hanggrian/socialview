package com.hendraanggrian.socialview.commons

import android.content.Context
import android.os.Build
import android.widget.ArrayAdapter
import android.widget.Filter
import java.util.*
import kotlin.collections.ArrayList

/**
 * An ArrayAdapter customized with Filter to display suggestions.
 * It is a direct parent of default [HashtagAdapter] and [MentionAdapter].

 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
abstract class SocialAdapter<T>(context: Context, resource: Int, textViewResourceId: Int) : ArrayAdapter<T>(context, resource, textViewResourceId, ArrayList<T>()) {

    private val tempItems: MutableList<T> = ArrayList()
    private val suggestions: MutableList<T> = ArrayList()

    override fun add(item: T?) {
        add(item, true)
    }

    override fun addAll(collection: Collection<T>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            super.addAll(collection)
            tempItems.addAll(collection)
        } else {
            throw UnsupportedOperationException("addAll() requires min SDK 11!")
        }
    }

    @SafeVarargs
    override fun addAll(vararg items: T) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            super.addAll(*items)
            Collections.addAll(tempItems, *items)
        } else {
            throw UnsupportedOperationException("addAll() requires min SDK 11!")
        }
    }

    override fun remove(`object`: T?) {
        super.remove(`object`)
        tempItems.remove(`object`)
    }

    override fun clear() {
        clear(true)
    }

    private fun add(item: T?, affectTempItems: Boolean) {
        super.add(item)
        if (affectTempItems) {
            tempItems.add(item!!)
        }
    }

    private fun clear(affectTempItems: Boolean) {
        super.clear()
        if (affectTempItems) {
            tempItems.clear()
        }
    }

    abstract inner class SocialFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
            if (constraint != null) {
                suggestions.clear()
                tempItems.forEach {
                    if (convertResultToString(it).toString().toLowerCase(Locale.US).contains(constraint.toString().toLowerCase(Locale.US))) {
                        suggestions.add(it)
                    }
                }
                val filterResults = Filter.FilterResults()
                filterResults.values = suggestions
                filterResults.count = suggestions.size
                return filterResults
            } else {
                return Filter.FilterResults()
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: Filter.FilterResults) {
            if (results.values != null) {
                val filterList = results.values as ArrayList<T>
                if (results.count > 0) {
                    clear(false)
                    for (item in filterList) {
                        add(item, false)
                    }
                    notifyDataSetChanged()
                }
            }
        }
    }
}