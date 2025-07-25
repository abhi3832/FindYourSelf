package com.example.findyourself.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    background = Color(10, 15, 26),
    surface = Color(10, 15, 26),
    onBackground = Color.White,
    onSurface = Color.White,
    primary = Color.White,
    primaryContainer = Color.White, // For selected chips and buttons
    onPrimaryContainer = Color.Black, // Text/icons on selected chips/buttons
    surfaceVariant = Color.DarkGray, // For unselected chips and cards
    onSurfaceVariant = Color.White // Text/icons on unselected chips
)

private val LightColorScheme = lightColorScheme(
    background = Color.White,
    surface = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    primary = Color.Black,
    primaryContainer = Color.White, // For selected chips and buttons
    onPrimaryContainer = Color.White, // Text/icons on selected chips/buttons
    surfaceVariant = Color.DarkGray, // For unselected chips and cards
    onSurfaceVariant = Color.Black // Text/icons on unselected chips
)

@Composable
fun FindYourselfTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}