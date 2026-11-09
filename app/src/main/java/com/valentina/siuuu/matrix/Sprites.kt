package com.valentina.siuuu.matrix

/**
 * The pixel art for the celebration, hand-drawn for a 25x25 circular matrix.
 *
 * Everything is an original silhouette: a generic footballer, a shirt number and a shout. Nobody's
 * likeness, photograph, logo or club badge is reproduced anywhere in this project.
 */
object Sprites {

    /** Run cycle, frame A: trailing leg back, arms swinging. */
    val RUN_A: Sprite = Sprite.of(
        "...###...",
        "..#####..",
        "..#####..",
        "...###...",
        "#...#....",
        ".#.###.#.",
        "..#####.#",
        "...###.#.",
        "..#####..",
        "..##.##..",
        ".###..##.",
        ".##....##",
        "###.....#",
    )

    /** Run cycle, frame B: legs crossing, arms wide. */
    val RUN_B: Sprite = Sprite.of(
        "...###...",
        "..#####..",
        "..#####..",
        "...###...",
        "..#.#.#..",
        "..#####..",
        "#..###..#",
        "#.#####.#",
        "..#####..",
        "...###...",
        "..##.##..",
        ".##...##.",
        "###...###",
    )

    /** Mid-air, knees tucked: the little jump before the landing. */
    val JUMP: Sprite = Sprite.of(
        "...###...",
        "..#####..",
        "..#####..",
        "...###...",
        "#..###..#",
        "#.#####.#",
        ".#######.",
        "..#####..",
        ".##...##.",
        "###...###",
        "##.....##",
    )

    /** The landing: feet planted wide, arms hanging out, chest up, chin high. */
    val CELEBRATION: Sprite = Sprite.of(
        ".....#####.....",
        "....#######....",
        "....#######....",
        "....#######....",
        ".....#####.....",
        "......###......",
        "...#########...",
        "..###########..",
        ".###.#####.###.",
        ".###..###..###.",
        ".##...###...##.",
        "......###......",
        ".....#####.....",
        "....###.###....",
        "...###...###...",
        "..###.....###..",
        ".####.....####.",
    )

    /** The shirt number: a bold seven with a crossbar. */
    val SEVEN: Sprite = Sprite.of(
        "#############",
        "#############",
        "#############",
        ".........###.",
        "........###..",
        ".......###...",
        "..#########..",
        "..#########..",
        "......###....",
        ".....###.....",
        ".....###.....",
        "....###......",
        "....###......",
        "...###.......",
        "...###.......",
    )
}
