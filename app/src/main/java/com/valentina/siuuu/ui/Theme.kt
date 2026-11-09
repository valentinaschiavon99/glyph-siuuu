package com.valentina.siuuu.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Ink = Color(0xFF000000)
private val Paper = Color(0xFFF5F5F5)
private val Accent = Color(0xFFD71921)
private val Surface = Color(0xFF111111)

private val DarkScheme = darkColorScheme(
    primary = Accent,
    onPrimary = Paper,
    background = Ink,
    onBackground = Paper,
    surface = Surface,
    onSurface = Paper,
)

private val LightScheme = lightColorScheme(
    primary = Accent,
    onPrimary = Paper,
    background = Paper,
    onBackground = Ink,
    surface = Color(0xFFEDEDED),
    onSurface = Ink,
)

@Composable
fun SiuuuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkScheme else LightScheme,
        content = content,
    )
}
