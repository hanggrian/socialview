package com.hendraanggrian.socialview.commons

/**
 * A data class of hashtag to be used with {@link HashtagAdapter}.
 *
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
data class Hashtag @JvmOverloads constructor(
        val hashtag: String,
        val count: Int? = null
)