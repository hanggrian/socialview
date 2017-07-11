package com.hendraanggrian.socialview.commons

/**
 * A data class of mention to be used with {@link MentionAdapter}.
 *
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
data class Mention @JvmOverloads constructor(
        val username: String,
        val displayname: String? = null,
        val avatar: Any? = null
)