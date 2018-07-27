package com.hendraanggrian.appcompat.socialview

/** A data class of mention to be used with [com.hendraanggrian.widget.MentionAdapter]. */
data class Mention @JvmOverloads constructor(
    override val username: String,
    override val displayname: String? = null,
    override val avatar: Any? = null
) : Mentionable {

    override fun equals(other: Any?): Boolean = other != null &&
        other is Mention &&
        other.username == username

    override fun hashCode(): Int = username.hashCode()

    override fun toString(): String = username
}