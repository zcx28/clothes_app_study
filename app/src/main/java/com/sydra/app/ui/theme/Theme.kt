package com.sydra.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SydraLightColors = lightColorScheme(
    primary = SydraBlack,
    onPrimary = SydraWhite,
    background = SydraWhite,
    onBackground = SydraBlack,
    surface = SydraWhite,
    onSurface = SydraBlack,
    onSurfaceVariant = SydraNeutral
)

private val SydraDarkColors = darkColorScheme(
    primary = SydraWhite,
    onPrimary = SydraBlack,
    background = Color(0xFF121212),
    onBackground = SydraWhite,
    surface = Color(0xFF121212),
    onSurface = SydraWhite,
    onSurfaceVariant = Color(0xFFB9B9B9)
)

@Composable
fun SydraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) SydraDarkColors else SydraLightColors,
        typography = SydraTypography,
        shapes = Shapes(),
        content = content
    )
}

