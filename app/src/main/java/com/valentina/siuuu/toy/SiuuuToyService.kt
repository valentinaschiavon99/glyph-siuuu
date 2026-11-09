package com.valentina.siuuu.toy

import android.content.Context
import com.nothing.ketchum.GlyphMatrixManager
import com.valentina.siuuu.animation.Celebration
import com.valentina.siuuu.glyph.GlyphToyService
import com.valentina.siuuu.matrix.MatrixCanvas
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * The toy itself.
 *
 * - selecting it plays the celebration once, then rests on the number
 * - a tap on the Glyph Button replays it
 * - a long press toggles a continuous loop
 * - as an AOD toy it just shows the number, dimmed
 */
class SiuuuToyService : GlyphToyService(TAG) {

    private var scope: CoroutineScope? = null
    private var playback: Job? = null
    private var looping = false

    override fun onGlyphMatrixReady(context: Context, manager: GlyphMatrixManager) {
        scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        play(loop = false)
    }

    override fun onGlyphMatrixReleased(context: Context) {
        playback = null
        looping = false
        scope?.cancel()
        scope = null
    }

    /** Tap: play it again from the top. */
    override fun onGlyphButtonPressed() {
        play(loop = looping)
    }

    /** Long press: keep it going until the next long press. */
    override fun onGlyphButtonLongPressed() {
        looping = !looping
        play(loop = looping)
    }

    override fun onAlwaysOnDisplayTick() {
        playback?.cancel()
        playback = scope?.launch { post(Celebration.renderIdle(AOD_BRIGHTNESS)) }
    }

    private fun play(loop: Boolean) {
        val scope = scope ?: return
        playback?.cancel()
        playback = scope.launch {
            // One canvas per playback, so a cancelled run can never draw into the next one.
            val canvas = MatrixCanvas()
            do {
                for (frame in 0 until Celebration.FRAME_COUNT) {
                    if (!isActive) return@launch
                    post(Celebration.render(frame, canvas))
                    delay(Celebration.FRAME_DURATION_MS)
                }
                if (loop) delay(LOOP_GAP_MS)
            } while (loop && isActive)
            post(Celebration.renderIdle(IDLE_BRIGHTNESS, canvas))
        }
    }

    /** `setMatrixFrame` wants the main thread; the frames are built off it. */
    private suspend fun post(frame: IntArray) = withContext(Dispatchers.Main) { showFrame(frame) }

    private companion object {
        const val TAG = "SiuuuToy"
        const val IDLE_BRIGHTNESS = 110
        const val AOD_BRIGHTNESS = 70
        const val LOOP_GAP_MS = 400L
    }
}
