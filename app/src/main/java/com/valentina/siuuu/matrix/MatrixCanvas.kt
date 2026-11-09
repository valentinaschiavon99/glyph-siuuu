package com.valentina.siuuu.matrix

import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.roundToInt

/**
 * A drawing surface for the Glyph Matrix on the back of the Nothing Phone (3).
 *
 * The matrix is addressed as a [SIZE] x [SIZE] grid (625 cells) but only the 489 cells that fall
 * inside a circle of radius 12.5 around the centre carry a physical LED. Anything drawn outside
 * that circle is dropped here, so the art is never silently clipped in a surprising way on device.
 *
 * Brightness is 0 (off) to [MAX_BRIGHTNESS] (full), matching `GlyphMatrixManager.setMatrixFrame`.
 * This class is pure Kotlin on purpose: it can be rendered on the JVM (see `tools/`) and inside a
 * Compose preview, not only on the phone.
 */
class MatrixCanvas {

    private val pixels = IntArray(LENGTH)

    fun clear() = pixels.fill(0)

    /** Lights a single LED, keeping the brighter of the old and new value. */
    fun light(x: Int, y: Int, brightness: Int) {
        if (!isLed(x, y)) return
        val value = brightness.coerceIn(0, MAX_BRIGHTNESS)
        val index = y * SIZE + x
        if (value > pixels[index]) pixels[index] = value
    }

    /** Draws [sprite] with its top-left corner at ([originX], [originY]). */
    fun draw(sprite: Sprite, originX: Int, originY: Int, brightness: Int = MAX_BRIGHTNESS) {
        for (y in 0 until sprite.height) {
            for (x in 0 until sprite.width) {
                if (sprite.isOn(x, y)) light(originX + x, originY + y, brightness)
            }
        }
    }

    /** Draws [sprite] horizontally centred, with its top edge at [originY]. */
    fun drawCentered(sprite: Sprite, originY: Int, brightness: Int = MAX_BRIGHTNESS) =
        draw(sprite, (SIZE - sprite.width) / 2, originY, brightness)

    /** Draws [text] with [PixelFont], left edge at [originX] (may be negative, for scrolling). */
    fun drawText(
        text: String,
        originX: Int,
        originY: Int,
        brightness: Int = MAX_BRIGHTNESS,
        tracking: Int = PixelFont.TRACKING,
    ) {
        var x = originX
        for (char in text) {
            val glyph = PixelFont.glyph(char)
            if (glyph != null) {
                draw(glyph, x, originY, brightness)
                x += glyph.width + tracking
            } else {
                x += PixelFont.SPACE_WIDTH + tracking
            }
        }
    }

    /** Draws a circle outline of [radius] around the centre: the landing shockwave. */
    fun drawRing(radius: Double, brightness: Int = MAX_BRIGHTNESS, thickness: Double = 1.6) {
        for (y in 0 until SIZE) {
            for (x in 0 until SIZE) {
                val distance = hypot((x - CENTER).toDouble(), (y - CENTER).toDouble())
                if (abs(distance - radius) <= thickness / 2.0) light(x, y, brightness)
            }
        }
    }

    /** Lights every LED: the goal flash. */
    fun fill(brightness: Int) {
        for (y in 0 until SIZE) for (x in 0 until SIZE) light(x, y, brightness)
    }

    /** A copy of the current frame, ready for `GlyphMatrixManager.setMatrixFrame`. */
    fun toFrame(): IntArray = pixels.copyOf()

    companion object {
        /** The Phone (3) matrix is addressed as 25x25. */
        const val SIZE = 25
        const val LENGTH = SIZE * SIZE
        const val CENTER = SIZE / 2
        const val MAX_BRIGHTNESS = 255

        private const val RADIUS = 12.5
        private const val RADIUS_SQUARED = RADIUS * RADIUS

        /** `true` for the 489 grid cells that actually have an LED behind them. */
        fun isLed(x: Int, y: Int): Boolean {
            if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) return false
            val dx = (x - CENTER).toDouble()
            val dy = (y - CENTER).toDouble()
            return dx * dx + dy * dy <= RADIUS_SQUARED
        }

        /** Sine wave between [low] and [high]: used to make held poses breathe. */
        fun breathe(tick: Int, low: Int, high: Int, period: Int): Int {
            val phase = 2.0 * Math.PI * tick / period
            return (low + (high - low) * (0.5 + 0.5 * kotlin.math.sin(phase))).roundToInt()
        }
    }
}
