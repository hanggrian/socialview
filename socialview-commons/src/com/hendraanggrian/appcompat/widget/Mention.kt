package com.hendraanggrian.appcompat.widget

/** A data class of mention to be used with [com.hendraanggrian.widget.MentionAdapter]. */
data class Mention @JvmOverloads constructor(

    /** Unique id of this mention. */
    val username: String,

    /** Optional display name, located above username. */
    val displayname: String? = null,

    /** Optional avatar, may be Drawable, resources, or string url pointing to image. */
    val avatar: Any? = null
) {

    override fun equals(other: Any?): Boolean = other != null &&
        other is Mention &&
        other.username == username

    override fun hashCode(): Int = username.hashCode()

    override fun toString(): String = username
}