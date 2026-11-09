package com.valentina.siuuu.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.valentina.siuuu.animation.Celebration
import com.valentina.siuuu.matrix.MatrixCanvas

/**
 * Draws a Glyph Matrix frame on screen: 489 dots in a circle, exactly the LEDs the phone has.
 * Handy while tweaking the pixel art, and it is the same [IntArray] the matrix receives.
 */
@Composable
fun MatrixPreview(
    frame: IntArray,
    modifier: Modifier = Modifier,
    ledColor: Color = Color.White,
    offColor: Color = Color(0xFF1A1A1A),
) {
    Canvas(modifier = modifier.aspectRatio(1f)) {
        val cell = size.minDimension / MatrixCanvas.SIZE
        val radius = cell * 0.38f
        for (y in 0 until MatrixCanvas.SIZE) {
            for (x in 0 until MatrixCanvas.SIZE) {
                if (!MatrixCanvas.isLed(x, y)) continue
                val value = frame.getOrElse(y * MatrixCanvas.SIZE + x) { 0 }
                val center = Offset((x + 0.5f) * cell, (y + 0.5f) * cell)
                if (value <= 0) {
                    drawCircle(offColor, radius, center)
                } else {
                    drawCircle(
                        ledColor.copy(alpha = value / MatrixCanvas.MAX_BRIGHTNESS.toFloat()),
                        radius,
                        center,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun MatrixPreviewPose() {
    MatrixPreview(frame = Celebration.render(28))
}
