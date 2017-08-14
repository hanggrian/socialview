package com.hendraanggrian.socialview

/**
 * A data class of mention to be used with [com.hendraanggrian.widget.MentionAdapter].
 *
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
data class Mention @JvmOverloads constructor(
        val username: String,
        val displayname: String? = null,
        val avatar: Any? = null
) {

    override fun equals(other: Any?) = other != null && other is Mention && other.username == username

    override fun hashCode() = username.hashCode()
}