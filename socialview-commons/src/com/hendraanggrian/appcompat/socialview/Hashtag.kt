package com.hendraanggrian.appcompat.socialview

/** A data class of hashtag to be used with [com.hendraanggrian.widget.HashtagAdapter]. */
data class Hashtag @JvmOverloads constructor(
    override val hashtag: String,
    override val count: Int? = null

) : Hashtagable {

    override fun equals(other: Any?): Boolean = other != null &&
        other is Hashtag &&
        other.hashtag == hashtag

    override fun hashCode(): Int = hashtag.hashCode()

    override fun toString(): String = hashtag
}