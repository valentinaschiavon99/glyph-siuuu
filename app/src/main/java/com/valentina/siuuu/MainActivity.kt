package com.valentina.siuuu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valentina.siuuu.animation.Celebration
import com.valentina.siuuu.matrix.MatrixCanvas
import com.valentina.siuuu.ui.MatrixPreview
import com.valentina.siuuu.ui.SiuuuTheme
import kotlinx.coroutines.delay

/**
 * There is nothing to do in the app itself: the animation lives on the back of the phone. This
 * screen only previews it and explains where to switch the toy on.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SiuuuTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    HomeScreen()
                }
            }
        }
    }
}

@Composable
private fun HomeScreen() {
    var playing by remember { mutableStateOf(true) }
    var frameIndex by remember { mutableIntStateOf(0) }
    val canvas = remember { MatrixCanvas() }
    var frame by remember { mutableStateOf(Celebration.render(0, canvas)) }

    LaunchedEffect(playing) {
        while (playing) {
            frame = Celebration.render(frameIndex, canvas)
            delay(Celebration.FRAME_DURATION_MS)
            frameIndex = (frameIndex + 1) % Celebration.FRAME_COUNT
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "SIUUUU",
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 34.sp,
            letterSpacing = 6.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = "a goal celebration for the Glyph Matrix",
            fontFamily = FontFamily.Monospace,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
        )
        Spacer(Modifier.height(28.dp))

        MatrixPreview(frame = frame, modifier = Modifier.fillMaxWidth(0.82f))

        Spacer(Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { playing = !playing }) {
                Text(if (playing) "Pause" else "Play")
            }
            OutlinedButton(onClick = { frameIndex = 0 }) {
                Text("Restart")
            }
        }

        Spacer(Modifier.height(32.dp))
        Instructions()
    }
}

@Composable
private fun Instructions() {
    val steps = listOf(
        "Install this app on a Nothing Phone (3).",
        "Open Settings > Glyph Interface > Glyph Toys.",
        "Enable SIUUUU and reorder it wherever you like.",
        "Press the Glyph Button on the back to cycle to it.",
        "Tap the button to replay, long-press to loop it.",
    )
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = "SETUP",
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            letterSpacing = 3.sp,
            color = MaterialTheme.colorScheme.primary,
        )
        steps.forEachIndexed { index, step ->
            Text(
                text = "${index + 1}.  $step",
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f),
            )
        }
    }
}
