package com.hendraanggrian.socialview

/** A data class of hashtag to be used with [com.hendraanggrian.widget.HashtagAdapter]. */
data class Hashtag @JvmOverloads constructor(
        val hashtag: String,
        val count: Int? = null
) {

    override fun equals(other: Any?): Boolean = other != null && other is Hashtag && other.hashtag == hashtag

    override fun hashCode(): Int = hashtag.hashCode()
}