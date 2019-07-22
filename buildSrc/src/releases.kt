const val RELEASE_USER = "hendraanggrian"
const val RELEASE_REPO = "appcompat"
const val RELEASE_GROUP = "com.$RELEASE_USER.$RELEASE_REPO"
const val RELEASE_ARTIFACT = "socialview"
const val RELEASE_VERSION = "0.3"
const val RELEASE_DESC = "Android TextView and EditText with hashtag, mention, and hyperlink support"
const val RELEASE_WEBSITE = "https://github.com/$RELEASE_USER/$RELEASE_ARTIFACT"

val BINTRAY_USER get() = System.getenv("BINTRAY_USER")
val BINTRAY_KEY get() = System.getenv("BINTRAY_KEY")