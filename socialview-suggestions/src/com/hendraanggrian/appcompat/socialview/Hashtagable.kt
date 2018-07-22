package com.hendraanggrian.appcompat.socialview

interface Hashtagable {

    /** Unique id of this hashtag. */
    val hashtag: String

    /** Optional count, located right to hashtag name. */
    val count: Int?
}