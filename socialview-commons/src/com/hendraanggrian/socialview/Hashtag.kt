package com.hendraanggrian.socialview

/**
 * A data class of hashtag to be used with [com.hendraanggrian.widget.HashtagAdapter].
 *
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
data class Hashtag @JvmOverloads constructor(
        val hashtag: String,
        val count: Int? = null
) {

    override fun equals(other: Any?) = other != null && other is Hashtag && other.hashtag == hashtag

    override fun hashCode() = hashtag.hashCode()
}