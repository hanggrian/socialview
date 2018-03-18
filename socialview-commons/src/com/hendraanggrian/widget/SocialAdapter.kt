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

    abstract fun T.convertToString(): String

    private var _filter: Filter? = null
    private val _items: MutableList<T> = ArrayList()
    private val _tempItems: MutableList<T> = ArrayList()

    override fun getFilter(): Filter {
        if (_filter == null) _filter = SocialFilter()
        return _filter!!
    }

    override fun add(item: T?) = add(item, true)

    override fun addAll(collection: Collection<T>) {
        super.addAll(collection)
        _tempItems.addAll(collection)
    }

    override fun addAll(vararg items: T) {
        super.addAll(*items)
        addAll(_tempItems, *items)
    }

    override fun remove(item: T?) {
        super.remove(item)
        _tempItems.remove(item)
    }

    override fun clear() = clear(true)

    private fun add(item: T?, affectTempItems: Boolean) {
        super.add(item)
        if (affectTempItems) _tempItems.add(item!!)
    }

    private fun clear(affectTempItems: Boolean) {
        super.clear()
        if (affectTempItems) _tempItems.clear()
    }

    private inner class SocialFilter : Filter() {

        override fun performFiltering(constraint: CharSequence?): Filter.FilterResults = when {
            constraint != null -> {
                _items.clear()
                _tempItems
                    .filter {
                        convertResultToString(it)
                            .toString()
                            .toLowerCase()
                            .contains(constraint.toString().toLowerCase())
                    }
                    .forEach { _items.add(it) }
                val filterResults = Filter.FilterResults()
                filterResults.values = _items
                filterResults.count = _items.size
                filterResults
            }
            else -> Filter.FilterResults()
        }

        override fun publishResults(constraint: CharSequence?, results: Filter.FilterResults) {
            if (results.values != null) {
                @Suppress("UNCHECKED_CAST")
                val filterList = results.values as ArrayList<T>
                if (results.count > 0) {
                    clear(false)
                    for (item in filterList) add(item, false)
                    notifyDataSetChanged()
                }
            }
        }

        override fun convertResultToString(resultValue: Any?): CharSequence =
            @Suppress("UNCHECKED_CAST") (resultValue as T).convertToString()
    }
}