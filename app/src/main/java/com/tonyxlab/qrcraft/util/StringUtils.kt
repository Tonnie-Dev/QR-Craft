package com.tonyxlab.qrcraft.util


fun generateLoremIpsum(wordCount: Int): String {
    val loremIpsumWords =
        listOf(
                "Lorem",
                "ipsum",
                "dolor",
                "sit",
                "amet",
                "consectetur",
                "adipiscing",
                "elit",
                "sed",
                "do",
        )

    return (0 until wordCount).joinToString(" ") {

        loremIpsumWords.random()
                .replaceFirstChar { it.titlecase() }
    }

}