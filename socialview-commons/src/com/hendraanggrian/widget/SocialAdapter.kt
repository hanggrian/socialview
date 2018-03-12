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

    private var mFilter: Filter? = null
    private val mItems: MutableList<T> = ArrayList()
    private val mTempItems: MutableList<T> = ArrayList()

    abstract fun T.convertToString(): String

    override fun getFilter(): Filter {
        if (mFilter == null) mFilter = object : SocialFilter() {
            override fun convertResultToString(resultValue: Any): CharSequence =
                @Suppress("UNCHECKED_CAST") (resultValue as T).convertToString()
        }
        return mFilter!!
    }

    override fun add(item: T?) = add(item, true)

    override fun addAll(collection: Collection<T>) {
        super.addAll(collection)
        mTempItems.addAll(collection)
    }

    override fun addAll(vararg items: T) {
        super.addAll(*items)
        addAll(mTempItems, *items)
    }

    override fun remove(item: T?) {
        super.remove(item)
        mTempItems.remove(item)
    }

    override fun clear() = clear(true)

    private fun add(item: T?, affectTempItems: Boolean) {
        super.add(item)
        if (affectTempItems) mTempItems.add(item!!)
    }

    private fun clear(affectTempItems: Boolean) {
        super.clear()
        if (affectTempItems) mTempItems.clear()
    }

    private abstract inner class SocialFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): Filter.FilterResults = when {
            constraint != null -> {
                mItems.clear()
                mTempItems.forEach {
                    if (convertResultToString(it)
                            .toString()
                            .toLowerCase()
                            .contains(constraint.toString().toLowerCase())) mItems.add(it)
                }
                val filterResults = Filter.FilterResults()
                filterResults.values = mItems
                filterResults.count = mItems.size
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
    }
}