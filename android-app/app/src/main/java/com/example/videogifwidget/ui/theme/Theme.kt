package com.example.videogifwidget.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color.White,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF2C2C2C),
    onPrimaryContainer = Color.White,
    secondary = Color(0xFFB0B0B0),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF3C3C3C),
    onSecondaryContainer = Color.White,
    tertiary = Color(0xFF808080),
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF4C4C4C),
    onTertiaryContainer = Color.White,
    error = Color(0xFFFF6B6B),
    errorContainer = Color(0xFF4C1D1D),
    onError = Color.Black,
    onErrorContainer = Color(0xFFFF6B6B),
    background = Color.Black,
    onBackground = Color.White,
    surface = Color(0xFF1C1C1C),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color(0xFFB0B0B0),
    outline = Color(0xFF808080),
    outlineVariant = Color(0xFF3C3C3C)
)

private val LightColorScheme = lightColorScheme(
    primary = Color.Black,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFF5F5F5),
    onPrimaryContainer = Color.Black,
    secondary = Color(0xFF424242),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF5F5F5),
    onSecondaryContainer = Color(0xFF424242),
    tertiary = Color(0xFF9E9E9E),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFF5F5F5),
    onTertiaryContainer = Color(0xFF424242),
    error = Color(0xFFD32F2F),
    errorContainer = Color(0xFFFFEBEE),
    onError = Color.White,
    onErrorContainer = Color(0xFFD32F2F),
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = Color(0xFF424242),
    outline = Color(0xFF9E9E9E),
    outlineVariant = Color(0xFFF5F5F5)
)

@Composable
fun VideoGifWidgetTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
