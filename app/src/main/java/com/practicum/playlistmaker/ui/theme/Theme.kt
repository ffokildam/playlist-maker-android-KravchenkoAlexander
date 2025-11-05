package com.practicum.playlistmaker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    background = LightBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    primary = Color(0xFF3772E7),
    surfaceVariant = LightSearchField // цвет поля поиска
)

private val DarkColors = darkColorScheme(
    background = DarkBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    primary = Color(0xFF3772E7),
    surfaceVariant = DarkSearchField // цвет поля поиска
)

@Composable
fun PlaylistMakerTheme(
    darkTheme: Boolean = androidx.compose.foundation.isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = androidx.compose.material3.Typography(),
        content = content
    )
}
