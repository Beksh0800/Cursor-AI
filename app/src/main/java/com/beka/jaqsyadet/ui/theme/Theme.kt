package com.beka.jaqsyadet.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary = Color(0xFF1F4B8E), // Deep Islamic Blue
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD5E3FF),
    onPrimaryContainer = Color(0xFF001B3F),
    secondary = Color(0xFF715573), // Mystical Purple
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFBD7FB),
    onSecondaryContainer = Color(0xFF2A132D),
    tertiary = Color(0xFF7B5732), // Warm Brown
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFDCC1),
    onTertiaryContainer = Color(0xFF2C1600),
    background = Color(0xFFFDFBFF),
    onBackground = Color(0xFF1A1C1E),
    surface = Color(0xFFFDFBFF),
    onSurface = Color(0xFF1A1C1E)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFA8C8FF), // Soft Islamic Blue
    onPrimary = Color(0xFF002F68),
    primaryContainer = Color(0xFF0A4488),
    onPrimaryContainer = Color(0xFFD5E3FF),
    secondary = Color(0xFFDEBCE0), // Soft Purple
    onSecondary = Color(0xFF402843),
    secondaryContainer = Color(0xFF573E5A),
    onSecondaryContainer = Color(0xFFFBD7FB),
    tertiary = Color(0xFFFFB77C), // Soft Brown
    onTertiary = Color(0xFF4A2800),
    tertiaryContainer = Color(0xFF623F1A),
    onTertiaryContainer = Color(0xFFFFDCC1),
    background = Color(0xFF1A1C1E),
    onBackground = Color(0xFFE3E2E6),
    surface = Color(0xFF1A1C1E),
    onSurface = Color(0xFFE3E2E6)
)

@Composable
fun JaqsyAdetTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
} 