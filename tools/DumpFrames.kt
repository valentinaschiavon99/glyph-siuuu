@file:JvmName("DumpFrames")

package tools

import com.valentina.siuuu.animation.Celebration
import com.valentina.siuuu.matrix.MatrixCanvas

/**
 * Dev helper: prints every frame of the celebration as a line of 625 brightness values, so the
 * animation can be inspected or turned into a GIF without a phone in hand.
 *
 * See tools/render_preview.sh.
 */
fun main(args: Array<String>) {
    val canvas = MatrixCanvas()
    if (args.contains("--ascii")) {
        val ramp = " .:-=+*#%@"
        for (index in 0 until Celebration.FRAME_COUNT) {
            val frame = Celebration.render(index, canvas)
            println("--- frame $index ---")
            for (y in 0 until MatrixCanvas.SIZE) {
                val row = StringBuilder()
                for (x in 0 until MatrixCanvas.SIZE) {
                    if (!MatrixCanvas.isLed(x, y)) {
                        row.append(' ')
                    } else {
                        val value = frame[y * MatrixCanvas.SIZE + x]
                        row.append(if (value == 0) '.' else ramp[minOf(9, value * 9 / 255)])
                    }
                }
                println(row)
            }
        }
        return
    }
    println("# frames=${Celebration.FRAME_COUNT} size=${MatrixCanvas.SIZE} ms=${Celebration.FRAME_DURATION_MS}")
    for (index in 0 until Celebration.FRAME_COUNT) {
        println(Celebration.render(index, canvas).joinToString(" "))
    }
}
