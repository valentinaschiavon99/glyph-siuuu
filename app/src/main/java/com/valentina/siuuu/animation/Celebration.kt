package com.valentina.siuuu.animation

import com.valentina.siuuu.matrix.MatrixCanvas
import com.valentina.siuuu.matrix.PixelFont
import com.valentina.siuuu.matrix.Sprites
import kotlin.math.roundToInt

/**
 * The choreography, as a pure function of a frame index.
 *
 * ```
 * run in  ->  jump  ->  landing flash + shockwave  ->  pose (breathing)  ->  7  ->  SIUUUU  ->  out
 * ```
 *
 * [render] is deterministic and allocation-light, so the same code drives the Glyph Matrix on the
 * phone, the on-screen preview in the app, and the GIF generator under `tools/`.
 */
object Celebration {

    /** One frame every 50 ms, i.e. 20 fps, which the matrix handles comfortably. */
    const val FRAME_DURATION_MS = 50L

    const val SHOUT = "SIUUUU!"

    private const val RUN_FRAMES = 14
    private const val JUMP_FRAMES = 5
    private const val IMPACT_FRAMES = 4
    private const val POSE_FRAMES = 16
    private const val SEVEN_WIPE_FRAMES = 8
    private const val SEVEN_HOLD_FRAMES = 14
    private const val FLASH_FRAMES = 4

    private const val RUN_START_X = 16
    private const val RUN_END_X = 8
    private const val RUN_TOP_Y = 7
    private const val JUMP_X = 8
    private const val JUMP_GROUND_Y = 10
    private const val POSE_TOP_Y = 4
    private const val SEVEN_TOP_Y = 5
    private const val TEXT_TOP_Y = 9

    private val JUMP_LIFT = intArrayOf(3, 5, 6, 5, 3)

    /** How far the shout travels: in from the right edge, out past the left. */
    private val MARQUEE_FRAMES = MatrixCanvas.SIZE + PixelFont.measure(SHOUT) + 4

    /** Length of one full celebration. */
    val FRAME_COUNT = RUN_FRAMES + JUMP_FRAMES + IMPACT_FRAMES + POSE_FRAMES +
        SEVEN_WIPE_FRAMES + SEVEN_HOLD_FRAMES + MARQUEE_FRAMES + FLASH_FRAMES

    /** Total wall-clock length of one celebration, in milliseconds. */
    val DURATION_MS = FRAME_COUNT * FRAME_DURATION_MS

    /**
     * Draws frame [frame] of the celebration into [canvas] and returns the matrix frame.
     * Indices outside `0 until FRAME_COUNT` wrap, so a looping caller can just keep counting.
     */
    fun render(frame: Int, canvas: MatrixCanvas = MatrixCanvas()): IntArray {
        canvas.clear()
        var t = frame.mod(FRAME_COUNT)

        // 1. Run in from the right, two-frame cycle with a little vertical bob.
        if (t < RUN_FRAMES) {
            val progress = t.toDouble() / RUN_FRAMES
            val x = (RUN_START_X - (RUN_START_X - RUN_END_X) * progress).roundToInt()
            val sprite = if ((t / 2) % 2 == 0) Sprites.RUN_A else Sprites.RUN_B
            canvas.draw(sprite, x, RUN_TOP_Y + (t % 2))
            return canvas.toFrame()
        }
        t -= RUN_FRAMES

        // 2. Up: knees tucked.
        if (t < JUMP_FRAMES) {
            canvas.draw(Sprites.JUMP, JUMP_X, JUMP_GROUND_Y - JUMP_LIFT[t])
            return canvas.toFrame()
        }
        t -= JUMP_FRAMES

        // 3. Landing: one white flash, then a shockwave expanding over the pose.
        if (t < IMPACT_FRAMES) {
            if (t == 0) {
                canvas.fill(MatrixCanvas.MAX_BRIGHTNESS)
            } else {
                canvas.drawCentered(Sprites.CELEBRATION, POSE_TOP_Y)
                canvas.drawRing(3.5 * t, (220 - 50 * t).coerceAtLeast(60))
            }
            return canvas.toFrame()
        }
        t -= IMPACT_FRAMES

        // 4. Hold the pose, breathing.
        if (t < POSE_FRAMES) {
            canvas.drawCentered(Sprites.CELEBRATION, POSE_TOP_Y, MatrixCanvas.breathe(t, 150, 255, 12))
            return canvas.toFrame()
        }
        t -= POSE_FRAMES

        // 5. The 7 rises into place from the bottom.
        if (t < SEVEN_WIPE_FRAMES) {
            val visibleRows = (Sprites.SEVEN.height * (t + 1).toDouble() / SEVEN_WIPE_FRAMES).roundToInt()
            val hidden = Sprites.SEVEN.height - visibleRows
            canvas.drawCentered(Sprites.SEVEN.bottomRows(visibleRows), SEVEN_TOP_Y + hidden)
            return canvas.toFrame()
        }
        t -= SEVEN_WIPE_FRAMES

        // 6. The 7 holds, breathing.
        if (t < SEVEN_HOLD_FRAMES) {
            canvas.drawCentered(Sprites.SEVEN, SEVEN_TOP_Y, MatrixCanvas.breathe(t, 170, 255, 14))
            return canvas.toFrame()
        }
        t -= SEVEN_HOLD_FRAMES

        // 7. SIUUUU scrolls through.
        if (t < MARQUEE_FRAMES) {
            canvas.drawText(SHOUT, MatrixCanvas.SIZE + 2 - t, TEXT_TOP_Y)
            return canvas.toFrame()
        }
        t -= MARQUEE_FRAMES

        // 8. Two quick strobes and we are done.
        canvas.fill(if (t % 2 == 0) MatrixCanvas.MAX_BRIGHTNESS else 50)
        return canvas.toFrame()
    }

    /** The resting frame shown between celebrations: just the number, dimmed. */
    fun renderIdle(brightness: Int = 110, canvas: MatrixCanvas = MatrixCanvas()): IntArray {
        canvas.clear()
        canvas.drawCentered(Sprites.SEVEN, SEVEN_TOP_Y, brightness)
        return canvas.toFrame()
    }
}
