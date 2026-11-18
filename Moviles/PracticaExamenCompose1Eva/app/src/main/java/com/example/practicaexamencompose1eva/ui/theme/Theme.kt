package com.example.practicaexamencompose1eva.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = PurplePrimaryLight,
    onPrimary = OnPurplePrimaryLight,
    primaryContainer = PurplePrimaryContainerLight,
    onPrimaryContainer = OnPurplePrimaryContainerLight,
    secondary = PurpleSecondaryLight,
    onSecondary = OnPurpleSecondaryLight,
    secondaryContainer = PurpleSecondaryContainerLight,
    onSecondaryContainer = OnPurpleSecondaryContainerLight,
    tertiary = GreenTertiaryLight,
    onTertiary = OnGreenTertiaryLight,
    tertiaryContainer = GreenTertiaryContainerLight,
    onTertiaryContainer = OnGreenTertiaryContainerLight,
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight,
    outlineVariant = OutlineVariantLight
)

private val DarkColorScheme = darkColorScheme(
    primary = PurplePrimaryDark,
    onPrimary = OnPurplePrimaryDark,
    primaryContainer = PurplePrimaryContainerDark,
    onPrimaryContainer = OnPurplePrimaryContainerDark,
    secondary = PurpleSecondaryDark,
    onSecondary = OnPurpleSecondaryDark,
    secondaryContainer = PurpleSecondaryContainerDark,
    onSecondaryContainer = OnPurpleSecondaryContainerDark,
    tertiary = GreenTertiaryDark,
    onTertiary = OnGreenTertiaryDark,
    tertiaryContainer = GreenTertiaryContainerDark,
    onTertiaryContainer = OnGreenTertiaryContainerDark,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark
)

@Composable
fun PracticaExamenCompose1EvaTheme(
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