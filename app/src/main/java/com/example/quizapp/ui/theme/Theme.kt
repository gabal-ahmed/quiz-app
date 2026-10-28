package com.example.quizapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/** Brand colors from the design system (section 7 of the spec). */
object Brand {
    val Navy = Color(0xFF07172F)
    val Blue = Color(0xFF4338F5)
    val Purple = Color(0xFF7C3AED)
    val PurpleLight = Color(0xFF9B7BFF)
    val Correct = Color(0xFF10B981)
    val Incorrect = Color(0xFFF0445E)
    val PrimaryText = Color(0xFFEAF0FF)
    val Gold = Color(0xFFFFC53D)
}

@Immutable
data class AppColors(
    val isDark: Boolean,
    val backgroundTop: Color,
    val backgroundBottom: Color,
    val surface: Color,
    val surfaceHigh: Color,
    val border: Color,
    val codeBackground: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    /** Purple used for highlighted text and icons; darker in light mode to stay readable. */
    val accent: Color,
)

val DarkAppColors = AppColors(
    isDark = true,
    backgroundTop = Brand.Navy,
    backgroundBottom = Color(0xFF0B1D45),
    surface = Color(0xFF0F2349),
    surfaceHigh = Color(0xFF162E5C),
    border = Color(0xFF223A6B),
    codeBackground = Color(0xFF081430),
    textPrimary = Brand.PrimaryText,
    textSecondary = Color(0xFF9AA8C7),
    accent = Brand.PurpleLight,
)

val LightAppColors = AppColors(
    isDark = false,
    backgroundTop = Color(0xFFF5F7FF),
    backgroundBottom = Color(0xFFE6EBFF),
    surface = Color(0xFFFFFFFF),
    surfaceHigh = Color(0xFFFFFFFF),
    border = Color(0xFFD6DDF5),
    codeBackground = Color(0xFFEEF1FA),
    textPrimary = Color(0xFF0B1630),
    textSecondary = Color(0xFF5B6785),
    accent = Brand.Purple,
)

val LocalAppColors = staticCompositionLocalOf { DarkAppColors }

object AppTheme {
    val colors: AppColors
        @Composable get() = LocalAppColors.current
}

@Composable
fun QuizAppTheme(darkTheme: Boolean = true, content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkAppColors else LightAppColors
    val scheme = if (darkTheme) {
        darkColorScheme(
            primary = Brand.Blue,
            onPrimary = Color.White,
            secondary = Brand.Purple,
            background = colors.backgroundTop,
            onBackground = colors.textPrimary,
            surface = colors.surface,
            onSurface = colors.textPrimary,
            onSurfaceVariant = colors.textSecondary,
            surfaceContainerHigh = colors.surfaceHigh,
            error = Brand.Incorrect,
        )
    } else {
        lightColorScheme(
            primary = Brand.Blue,
            onPrimary = Color.White,
            secondary = Brand.Purple,
            background = colors.backgroundTop,
            onBackground = colors.textPrimary,
            surface = colors.surface,
            onSurface = colors.textPrimary,
            onSurfaceVariant = colors.textSecondary,
            surfaceContainerHigh = colors.surfaceHigh,
            error = Brand.Incorrect,
        )
    }
    CompositionLocalProvider(LocalAppColors provides colors) {
        MaterialTheme(colorScheme = scheme, content = content)
    }
}
