package com.example

import com.hanggrian.socialview.autocomplete.Mention

object Mentions : List<Mention> by listOf(
    Mention("ladybirdhill"),
    Mention("Hank Hill", "hankhill"),
    Mention(
        "bobbyhill",
        "Bobby Hill",
        "https://static.wikia.nocookie.net/kingofthehill/images/c/c7/Bobby.png/revision/latest/" +
            "smart/width/400/height/300/",
    ),
)
