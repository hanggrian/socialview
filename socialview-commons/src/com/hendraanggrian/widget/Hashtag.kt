package com.hendraanggrian.widget

/** A data class of hashtag to be used with [com.hendraanggrian.widget.HashtagAdapter]. */
data class Hashtag @JvmOverloads constructor(

    /** Unique id of this hashtag. */
    val hashtag: String,

    /** Optional count, located right to hashtag name. */
    val count: Int? = null
) {

    override fun equals(other: Any?): Boolean = other != null &&
        other is Hashtag &&
        other.hashtag == hashtag

    override fun hashCode(): Int = hashtag.hashCode()
}