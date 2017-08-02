@file:JvmName("Hashtag")

package com.hendraanggrian.socialview.commons

/**
 * A data class of hashtag to be used with [HashtagAdapter].
 *
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
data class Hashtag @JvmOverloads constructor(
        val hashtag: String,
        val count: Int? = null)