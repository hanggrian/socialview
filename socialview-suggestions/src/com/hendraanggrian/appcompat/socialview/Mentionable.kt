package com.hendraanggrian.appcompat.socialview

interface Mentionable {

    /** Unique id of this mention. */
    val username: String

    /** Optional display name, located above username. */
    val displayname: String?

    /** Optional avatar, may be Drawable, resources, or string url pointing to image. */
    val avatar: Any?
}