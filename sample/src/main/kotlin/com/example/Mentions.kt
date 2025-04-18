package com.example

import com.hanggrian.socialview.autocomplete.Mention

object Mentions : List<Mention> by listOf(
    Mention(
        "hankhill",
        null,
        "https://static.wikia.nocookie.net/mrmsco/images/d/d9/Hank_Hill.jpg/revision/latest/",
    ),
    Mention(
        "peggyhill",
        "Peggy Hill",
        "https://static.wikia.nocookie.net/kingofthehill/images/a/a7/Peggy-hill.jpg/" +
            "revision/latest/",
    ),
    Mention(
        "bobbyhill",
        "Bobby Hill",
        "https://static.wikia.nocookie.net/kingofthehill/images/c/c7/Bobby.png/revision/latest/" +
            "smart/width/400/height/300/",
    ),
)
