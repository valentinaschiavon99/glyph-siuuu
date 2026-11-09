package com.valentina.siuuu.matrix

/**
 * A tiny piece of monochrome pixel art, written as text so it can be edited by hand.
 *
 * `#` (or any non-blank, non-`.` character) means "LED on", `.` and spaces mean "LED off".
 * Rows are padded to the width of the widest row, so the art stays readable in source.
 */
class Sprite private constructor(val rows: List<String>) {

    val height: Int get() = rows.size
    val width: Int get() = if (rows.isEmpty()) 0 else rows[0].length

    /** `true` when the pixel at [x], [y] of this sprite should be lit. */
    fun isOn(x: Int, y: Int): Boolean {
        if (y < 0 || y >= height || x < 0 || x >= width) return false
        val c = rows[y][x]
        return c != '.' && c != ' '
    }

    /** The bottom [count] rows, used for reveal/wipe effects. */
    fun bottomRows(count: Int): Sprite =
        of(rows.subList((height - count).coerceIn(0, height), height))

    companion object {
        fun of(vararg rows: String): Sprite = of(rows.toList())

        fun of(rows: List<String>): Sprite {
            val width = rows.maxOfOrNull { it.length } ?: 0
            return Sprite(rows.map { it.padEnd(width, '.') })
        }
    }
}
