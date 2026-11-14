package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = SagePrimaryDark,
    onPrimary = OnSagePrimaryDark,
    primaryContainer = SagePrimaryContainerDark,
    onPrimaryContainer = OnSagePrimaryContainerDark,
    secondary = BrassSecondaryDark,
    onSecondary = OnBrassSecondaryDark,
    secondaryContainer = BrassSecondaryContainerDark,
    onSecondaryContainer = OnBrassSecondaryContainerDark,
    tertiary = SlateTertiaryDark,
    onTertiary = OnSlateTertiaryDark,
    tertiaryContainer = SlateTertiaryContainerDark,
    onTertiaryContainer = OnSlateTertiaryContainerDark,
    background = ObsidianBackgroundDark,
    onBackground = OnObsidianBackgroundDark,
    surface = ObsidianSurfaceDark,
    onSurface = OnObsidianSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark
)

private val LightColorScheme = lightColorScheme(
    primary = SagePrimaryLight,
    onPrimary = OnSagePrimaryLight,
    primaryContainer = SagePrimaryContainerLight,
    onPrimaryContainer = OnSagePrimaryContainerLight,
    secondary = BrassSecondaryLight,
    onSecondary = OnBrassSecondaryLight,
    secondaryContainer = BrassSecondaryContainerLight,
    onSecondaryContainer = OnBrassSecondaryContainerLight,
    tertiary = SlateTertiaryLight,
    onTertiary = OnSlateTertiaryLight,
    tertiaryContainer = SlateTertiaryContainerLight,
    onTertiaryContainer = OnSlateTertiaryContainerLight,
    background = ParchmentBackgroundLight,
    onBackground = OnParchmentBackgroundLight,
    surface = WarmSurfaceLight,
    onSurface = OnWarmSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight,
    outlineVariant = OutlineVariantLight
)

@Composable
fun SageCounselTheme(
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
