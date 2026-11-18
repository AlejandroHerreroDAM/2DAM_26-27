package com.example.practicacheckboxescompose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Esquema fijo para modo claro.
private val LightColorScheme = lightColorScheme(
    primary = IndigoPrimary,
    onPrimary = IndigoOnPrimary,
    primaryContainer = IndigoPrimaryContainer,
    onPrimaryContainer = IndigoOnPrimaryContainer,
    tertiary = GreenTertiary,
    tertiaryContainer = GreenTertiaryContainer,
    onTertiaryContainer = GreenOnTertiaryContainer,
    error = RedError,
    errorContainer = RedErrorContainer,
    onErrorContainer = RedOnErrorContainer,
    surface = NeutralSurface,
    onSurface = NeutralOnSurface
)

// Esquema fijo para modo oscuro.
private val DarkColorScheme = darkColorScheme(
    primary = IndigoPrimaryContainer,
    onPrimary = IndigoOnPrimaryContainer,
    primaryContainer = IndigoPrimary,
    onPrimaryContainer = IndigoOnPrimary,
    tertiary = GreenTertiaryContainer,
    tertiaryContainer = GreenTertiary,
    onTertiaryContainer = GreenOnTertiaryContainer,
    error = RedErrorContainer,
    errorContainer = RedError,
    onErrorContainer = RedErrorContainer
)

@Composable
fun PracticaCheckboxesComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // No usa colores dinamicos: siempre aplica la paleta personalizada.
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(colorScheme = colorScheme, typography = AppTypography, content = content)
}
