package com.example.multiplechoicesrs.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = LightBlue,
    onPrimary = MutedBlack,
    background = MutedBlack,
    onBackground = MutedWhite,
    primaryContainer = LightBlue,
    onPrimaryContainer = MutedBlack,
)

private val DarkCustomColorPalette = CustomColorPalette(
    cardViewBorder = CardViewBorderDark,
)

private val LightColorScheme = lightColorScheme(
    primary = DarkBlue,
    onPrimary = MutedWhite,
    background = MutedWhite,
    onBackground = MutedBlack,
    primaryContainer = DarkBlue,
    onPrimaryContainer = MutedWhite,

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

private val LightCustomColorPalette = CustomColorPalette(
    cardViewBorder = Color.Transparent,
)

@Composable
fun MultipleChoiceSRSTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    var colorScheme: ColorScheme
    var customColorPalette: CustomColorPalette

    if (darkTheme) {
        colorScheme = DarkColorScheme
        customColorPalette = DarkCustomColorPalette
    } else {
        colorScheme = LightColorScheme
        customColorPalette = LightCustomColorPalette
    }


    CompositionLocalProvider(
        LocalCustomColorPalette provides customColorPalette
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}