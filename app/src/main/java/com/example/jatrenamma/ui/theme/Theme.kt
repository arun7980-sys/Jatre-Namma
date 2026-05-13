package com.example.jatrenamma.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Saffron,
    secondary = Marigold,
    tertiary = FestiveGold,
    background = DeepBrown,
    surface = DeepBrown,
    onPrimary = Color.White,
    onSecondary = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = FestiveOrange,
    secondary = Marigold,
    tertiary = Saffron,
    background = FestiveCream,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun JatreNammaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
