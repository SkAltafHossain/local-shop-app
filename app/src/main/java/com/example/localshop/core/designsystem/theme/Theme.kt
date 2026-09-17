package com.example.localshop.core.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Custom Theme data class
data class AppColors(
    val primary: Color,
    val primaryText: Color,
    val secondaryText: Color,
    val pageBackground: Color,
    val primaryButton: Color,
    val secondaryButton: Color,
    val otherText: Color,
    val surface: Color,
    val onSurface: Color,
    val onPrimary: Color,
    val error: Color,
    val success: Color,
    val warning: Color,
    val assent: Color,
    val white: Color,
    val black: Color,
    val red: Color
)

// Light Theme
val LightAppColors = AppColors(
    primary = LightPrimary,
    primaryText = LightPrimaryText,
    secondaryText = LightSecondaryText,
    pageBackground = LightPageBackground,
    primaryButton = LightPrimaryButton,
    secondaryButton = LightSecondaryButton,
    otherText = LightOtherText,
    surface = LightSurface,
    onSurface = LightOnSurface,
    onPrimary = LightOnPrimary,
    error = LightError,
    success = LightSuccess,
    warning = LightWarning,
    assent = LightAssent,
    white = White,
    black = Black,
    red = Red
)

// Dark Theme
val DarkAppColors = AppColors(
    primary = DarkPrimary,
    primaryText = DarkPrimaryText,
    secondaryText = DarkSecondaryText,
    pageBackground = DarkPageBackground,
    primaryButton = DarkPrimaryButton,
    secondaryButton = DarkSecondaryButton,
    otherText = DarkOtherText,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    onPrimary = DarkOnPrimary,
    error = DarkError,
    success = DarkSuccess,
    warning = DarkWarning,
    assent = DarkAssent,
    white = White,
    black = Black,
    red = Red
)

// Composition Local for custom colors
val LocalAppColors = androidx.compose.runtime.compositionLocalOf<AppColors> {
    error("No AppColors provided")
}

@Composable
fun LocalShopTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkAppColors else LightAppColors
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.pageBackground.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(
        LocalAppColors provides colors
    ) {
        MaterialTheme(
            typography = Typography,
            content = content
        )
    }
}

// Helper object to access theme colors
object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current
}