package com.valentina.siuuu.matrix

/**
 * A 5x7 bitmap font, big enough to stay legible on 25 LEDs of width and short enough to fit the
 * widest band of the matrix circle. Only the characters the toy needs are defined; add more rows
 * here and they become available to [MatrixCanvas.drawText].
 */
object PixelFont {

    const val TRACKING = 1
    const val SPACE_WIDTH = 3
    const val HEIGHT = 7

    private val glyphs: Map<Char, Sprite> = mapOf(
        'S' to Sprite.of(
            ".####",
            "#....",
            "#....",
            ".###.",
            "....#",
            "....#",
            "####.",
        ),
        'I' to Sprite.of(
            "#####",
            "..#..",
            "..#..",
            "..#..",
            "..#..",
            "..#..",
            "#####",
        ),
        'U' to Sprite.of(
            "#...#",
            "#...#",
            "#...#",
            "#...#",
            "#...#",
            "#...#",
            ".###.",
        ),
        'G' to Sprite.of(
            ".###.",
            "#...#",
            "#....",
            "#.###",
            "#...#",
            "#...#",
            ".###.",
        ),
        'O' to Sprite.of(
            ".###.",
            "#...#",
            "#...#",
            "#...#",
            "#...#",
            "#...#",
            ".###.",
        ),
        'L' to Sprite.of(
            "#....",
            "#....",
            "#....",
            "#....",
            "#....",
            "#....",
            "#####",
        ),
        '7' to Sprite.of(
            "#####",
            "....#",
            "...#.",
            "..###",
            "..#..",
            ".#...",
            ".#...",
        ),
        '!' to Sprite.of(
            "..#..",
            "..#..",
            "..#..",
            "..#..",
            "..#..",
            ".....",
            "..#..",
        ),
    )

    fun glyph(char: Char): Sprite? = glyphs[char.uppercaseChar()]

    /** Width in LEDs of [text] once rendered, tracking included. */
    fun measure(text: String, tracking: Int = TRACKING): Int {
        if (text.isEmpty()) return 0
        var width = -tracking
        for (char in text) {
            width += (glyph(char)?.width ?: SPACE_WIDTH) + tracking
        }
        return width
    }
}
