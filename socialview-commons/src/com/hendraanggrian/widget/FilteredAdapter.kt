package com.hendraanggrian.widget

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import java.util.Collections.addAll
import java.util.Locale.US

/**
 * An ArrayAdapter customized with Filter to display items.
 * It is a direct parent of default [HashtagAdapter] and [MentionAdapter].
 */
abstract class FilteredAdapter<T>(
        context: Context,
        resource: Int,
        textViewResourceId: Int
) : ArrayAdapter<T>(context, resource, textViewResourceId, ArrayList<T>()) {

    private val items: MutableList<T> = ArrayList()
    private val tempItems: MutableList<T> = ArrayList()

    override fun add(item: T?) = add(item, true)

    override fun addAll(collection: Collection<T>) {
        super.addAll(collection)
        tempItems.addAll(collection)
    }

    @SafeVarargs
    override fun addAll(vararg items: T) {
        super.addAll(*items)
        addAll(tempItems, *items)
    }

    override fun remove(item: T?) {
        super.remove(item)
        tempItems.remove(item)
    }

    override fun clear() = clear(true)

    private fun add(item: T?, affectTempItems: Boolean) {
        super.add(item)
        if (affectTempItems) tempItems.add(item!!)
    }

    private fun clear(affectTempItems: Boolean) {
        super.clear()
        if (affectTempItems) tempItems.clear()
    }

    abstract inner class SocialFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): Filter.FilterResults = if (constraint != null) {
            items.clear()
            tempItems.forEach { if (convertResultToString(it).toString().toLowerCase(US).contains(constraint.toString().toLowerCase(US))) items.add(it) }
            val filterResults = Filter.FilterResults()
            filterResults.values = items
            filterResults.count = items.size
            filterResults
        } else {
            Filter.FilterResults()
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: Filter.FilterResults) {
            if (results.values != null) {
                val filterList = results.values as ArrayList<T>
                if (results.count > 0) {
                    clear(false)
                    for (item in filterList) add(item, false)
                    notifyDataSetChanged()
                }
            }
        }
    }
}