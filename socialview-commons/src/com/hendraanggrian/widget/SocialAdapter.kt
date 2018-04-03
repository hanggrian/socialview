package com.hendraanggrian.widget

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import java.util.Collections.addAll

/**
 * An ArrayAdapter customized with filter to display items.
 * It is a direct parent of default [HashtagAdapter] and [MentionAdapter],
 * which are optional adapters.
 */
abstract class SocialAdapter<T>(
    context: Context,
    resource: Int,
    textViewResourceId: Int
) : ArrayAdapter<T>(context, resource, textViewResourceId, ArrayList<T>()) {

    private var filter: Filter? = null
    private val items: MutableList<T> = mutableListOf()
    private val tempItems: MutableList<T> = mutableListOf()

    override fun getFilter(): Filter {
        if (filter == null) filter = SocialFilter()
        return filter!!
    }

    override fun add(item: T?) = add(item, true)

    override fun addAll(collection: Collection<T>) {
        super.addAll(collection)
        tempItems.addAll(collection)
    }

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

    private inner class SocialFilter : Filter() {

        override fun performFiltering(s: CharSequence?): FilterResults = when (s) {
            null -> FilterResults()
            else -> {
                items.clear()
                items += tempItems
                    .filter {
                        convertResultToString(it)
                            .toString()
                            .toLowerCase()
                            .contains(s.toString().toLowerCase())
                    }
                FilterResults().apply {
                    values = items
                    count = items.size
                }
            }
        }

        override fun publishResults(s: CharSequence?, results: FilterResults) {
            @Suppress("UNCHECKED_CAST") (results.values as? ArrayList<T>)?.let {
                if (results.count > 0) {
                    clear(false)
                    it.forEach { add(it, false) }
                    notifyDataSetChanged()
                }
            }
        }

        override fun convertResultToString(resultValue: Any?): CharSequence = resultValue.toString()
    }
}