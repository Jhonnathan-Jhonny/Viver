package com.example.compose
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.project.viver.ui.theme.AppTypography
import com.project.viver.ui.theme.backgroundDark
import com.project.viver.ui.theme.backgroundDarkHighContrast
import com.project.viver.ui.theme.backgroundDarkMediumContrast
import com.project.viver.ui.theme.backgroundLight
import com.project.viver.ui.theme.backgroundLightHighContrast
import com.project.viver.ui.theme.backgroundLightMediumContrast
import com.project.viver.ui.theme.errorContainerDark
import com.project.viver.ui.theme.errorContainerDarkHighContrast
import com.project.viver.ui.theme.errorContainerDarkMediumContrast
import com.project.viver.ui.theme.errorContainerLight
import com.project.viver.ui.theme.errorContainerLightHighContrast
import com.project.viver.ui.theme.errorContainerLightMediumContrast
import com.project.viver.ui.theme.errorDark
import com.project.viver.ui.theme.errorDarkHighContrast
import com.project.viver.ui.theme.errorDarkMediumContrast
import com.project.viver.ui.theme.errorLight
import com.project.viver.ui.theme.errorLightHighContrast
import com.project.viver.ui.theme.errorLightMediumContrast
import com.project.viver.ui.theme.inverseOnSurfaceDark
import com.project.viver.ui.theme.inverseOnSurfaceDarkHighContrast
import com.project.viver.ui.theme.inverseOnSurfaceDarkMediumContrast
import com.project.viver.ui.theme.inverseOnSurfaceLight
import com.project.viver.ui.theme.inverseOnSurfaceLightHighContrast
import com.project.viver.ui.theme.inverseOnSurfaceLightMediumContrast
import com.project.viver.ui.theme.inversePrimaryDark
import com.project.viver.ui.theme.inversePrimaryDarkHighContrast
import com.project.viver.ui.theme.inversePrimaryDarkMediumContrast
import com.project.viver.ui.theme.inversePrimaryLight
import com.project.viver.ui.theme.inversePrimaryLightHighContrast
import com.project.viver.ui.theme.inversePrimaryLightMediumContrast
import com.project.viver.ui.theme.inverseSurfaceDark
import com.project.viver.ui.theme.inverseSurfaceDarkHighContrast
import com.project.viver.ui.theme.inverseSurfaceDarkMediumContrast
import com.project.viver.ui.theme.inverseSurfaceLight
import com.project.viver.ui.theme.inverseSurfaceLightHighContrast
import com.project.viver.ui.theme.inverseSurfaceLightMediumContrast
import com.project.viver.ui.theme.onBackgroundDark
import com.project.viver.ui.theme.onBackgroundDarkHighContrast
import com.project.viver.ui.theme.onBackgroundDarkMediumContrast
import com.project.viver.ui.theme.onBackgroundLight
import com.project.viver.ui.theme.onBackgroundLightHighContrast
import com.project.viver.ui.theme.onBackgroundLightMediumContrast
import com.project.viver.ui.theme.onErrorContainerDark
import com.project.viver.ui.theme.onErrorContainerDarkHighContrast
import com.project.viver.ui.theme.onErrorContainerDarkMediumContrast
import com.project.viver.ui.theme.onErrorContainerLight
import com.project.viver.ui.theme.onErrorContainerLightHighContrast
import com.project.viver.ui.theme.onErrorContainerLightMediumContrast
import com.project.viver.ui.theme.onErrorDark
import com.project.viver.ui.theme.onErrorDarkHighContrast
import com.project.viver.ui.theme.onErrorDarkMediumContrast
import com.project.viver.ui.theme.onErrorLight
import com.project.viver.ui.theme.onErrorLightHighContrast
import com.project.viver.ui.theme.onErrorLightMediumContrast
import com.project.viver.ui.theme.onPrimaryContainerDark
import com.project.viver.ui.theme.onPrimaryContainerDarkHighContrast
import com.project.viver.ui.theme.onPrimaryContainerDarkMediumContrast
import com.project.viver.ui.theme.onPrimaryContainerLight
import com.project.viver.ui.theme.onPrimaryContainerLightHighContrast
import com.project.viver.ui.theme.onPrimaryContainerLightMediumContrast
import com.project.viver.ui.theme.onPrimaryDark
import com.project.viver.ui.theme.onPrimaryDarkHighContrast
import com.project.viver.ui.theme.onPrimaryDarkMediumContrast
import com.project.viver.ui.theme.onPrimaryLight
import com.project.viver.ui.theme.onPrimaryLightHighContrast
import com.project.viver.ui.theme.onPrimaryLightMediumContrast
import com.project.viver.ui.theme.onSecondaryContainerDark
import com.project.viver.ui.theme.onSecondaryContainerDarkHighContrast
import com.project.viver.ui.theme.onSecondaryContainerDarkMediumContrast
import com.project.viver.ui.theme.onSecondaryContainerLight
import com.project.viver.ui.theme.onSecondaryContainerLightHighContrast
import com.project.viver.ui.theme.onSecondaryContainerLightMediumContrast
import com.project.viver.ui.theme.onSecondaryDark
import com.project.viver.ui.theme.onSecondaryDarkHighContrast
import com.project.viver.ui.theme.onSecondaryDarkMediumContrast
import com.project.viver.ui.theme.onSecondaryLight
import com.project.viver.ui.theme.onSecondaryLightHighContrast
import com.project.viver.ui.theme.onSecondaryLightMediumContrast
import com.project.viver.ui.theme.onSurfaceDark
import com.project.viver.ui.theme.onSurfaceDarkHighContrast
import com.project.viver.ui.theme.onSurfaceDarkMediumContrast
import com.project.viver.ui.theme.onSurfaceLight
import com.project.viver.ui.theme.onSurfaceLightHighContrast
import com.project.viver.ui.theme.onSurfaceLightMediumContrast
import com.project.viver.ui.theme.onSurfaceVariantDark
import com.project.viver.ui.theme.onSurfaceVariantDarkHighContrast
import com.project.viver.ui.theme.onSurfaceVariantDarkMediumContrast
import com.project.viver.ui.theme.onSurfaceVariantLight
import com.project.viver.ui.theme.onSurfaceVariantLightHighContrast
import com.project.viver.ui.theme.onSurfaceVariantLightMediumContrast
import com.project.viver.ui.theme.onTertiaryContainerDark
import com.project.viver.ui.theme.onTertiaryContainerDarkHighContrast
import com.project.viver.ui.theme.onTertiaryContainerDarkMediumContrast
import com.project.viver.ui.theme.onTertiaryContainerLight
import com.project.viver.ui.theme.onTertiaryContainerLightHighContrast
import com.project.viver.ui.theme.onTertiaryContainerLightMediumContrast
import com.project.viver.ui.theme.onTertiaryDark
import com.project.viver.ui.theme.onTertiaryDarkHighContrast
import com.project.viver.ui.theme.onTertiaryDarkMediumContrast
import com.project.viver.ui.theme.onTertiaryLight
import com.project.viver.ui.theme.onTertiaryLightHighContrast
import com.project.viver.ui.theme.onTertiaryLightMediumContrast
import com.project.viver.ui.theme.outlineDark
import com.project.viver.ui.theme.outlineDarkHighContrast
import com.project.viver.ui.theme.outlineDarkMediumContrast
import com.project.viver.ui.theme.outlineLight
import com.project.viver.ui.theme.outlineLightHighContrast
import com.project.viver.ui.theme.outlineLightMediumContrast
import com.project.viver.ui.theme.outlineVariantDark
import com.project.viver.ui.theme.outlineVariantDarkHighContrast
import com.project.viver.ui.theme.outlineVariantDarkMediumContrast
import com.project.viver.ui.theme.outlineVariantLight
import com.project.viver.ui.theme.outlineVariantLightHighContrast
import com.project.viver.ui.theme.outlineVariantLightMediumContrast
import com.project.viver.ui.theme.primaryContainerDark
import com.project.viver.ui.theme.primaryContainerDarkHighContrast
import com.project.viver.ui.theme.primaryContainerDarkMediumContrast
import com.project.viver.ui.theme.primaryContainerLight
import com.project.viver.ui.theme.primaryContainerLightHighContrast
import com.project.viver.ui.theme.primaryContainerLightMediumContrast
import com.project.viver.ui.theme.primaryDark
import com.project.viver.ui.theme.primaryDarkHighContrast
import com.project.viver.ui.theme.primaryDarkMediumContrast
import com.project.viver.ui.theme.primaryLight
import com.project.viver.ui.theme.primaryLightHighContrast
import com.project.viver.ui.theme.primaryLightMediumContrast
import com.project.viver.ui.theme.scrimDark
import com.project.viver.ui.theme.scrimDarkHighContrast
import com.project.viver.ui.theme.scrimDarkMediumContrast
import com.project.viver.ui.theme.scrimLight
import com.project.viver.ui.theme.scrimLightHighContrast
import com.project.viver.ui.theme.scrimLightMediumContrast
import com.project.viver.ui.theme.secondaryContainerDark
import com.project.viver.ui.theme.secondaryContainerDarkHighContrast
import com.project.viver.ui.theme.secondaryContainerDarkMediumContrast
import com.project.viver.ui.theme.secondaryContainerLight
import com.project.viver.ui.theme.secondaryContainerLightHighContrast
import com.project.viver.ui.theme.secondaryContainerLightMediumContrast
import com.project.viver.ui.theme.secondaryDark
import com.project.viver.ui.theme.secondaryDarkHighContrast
import com.project.viver.ui.theme.secondaryDarkMediumContrast
import com.project.viver.ui.theme.secondaryLight
import com.project.viver.ui.theme.secondaryLightHighContrast
import com.project.viver.ui.theme.secondaryLightMediumContrast
import com.project.viver.ui.theme.surfaceBrightDark
import com.project.viver.ui.theme.surfaceBrightDarkHighContrast
import com.project.viver.ui.theme.surfaceBrightDarkMediumContrast
import com.project.viver.ui.theme.surfaceBrightLight
import com.project.viver.ui.theme.surfaceBrightLightHighContrast
import com.project.viver.ui.theme.surfaceBrightLightMediumContrast
import com.project.viver.ui.theme.surfaceContainerDark
import com.project.viver.ui.theme.surfaceContainerDarkHighContrast
import com.project.viver.ui.theme.surfaceContainerDarkMediumContrast
import com.project.viver.ui.theme.surfaceContainerHighDark
import com.project.viver.ui.theme.surfaceContainerHighDarkHighContrast
import com.project.viver.ui.theme.surfaceContainerHighDarkMediumContrast
import com.project.viver.ui.theme.surfaceContainerHighLight
import com.project.viver.ui.theme.surfaceContainerHighLightHighContrast
import com.project.viver.ui.theme.surfaceContainerHighLightMediumContrast
import com.project.viver.ui.theme.surfaceContainerHighestDark
import com.project.viver.ui.theme.surfaceContainerHighestDarkHighContrast
import com.project.viver.ui.theme.surfaceContainerHighestDarkMediumContrast
import com.project.viver.ui.theme.surfaceContainerHighestLight
import com.project.viver.ui.theme.surfaceContainerHighestLightHighContrast
import com.project.viver.ui.theme.surfaceContainerHighestLightMediumContrast
import com.project.viver.ui.theme.surfaceContainerLight
import com.project.viver.ui.theme.surfaceContainerLightHighContrast
import com.project.viver.ui.theme.surfaceContainerLightMediumContrast
import com.project.viver.ui.theme.surfaceContainerLowDark
import com.project.viver.ui.theme.surfaceContainerLowDarkHighContrast
import com.project.viver.ui.theme.surfaceContainerLowDarkMediumContrast
import com.project.viver.ui.theme.surfaceContainerLowLight
import com.project.viver.ui.theme.surfaceContainerLowLightHighContrast
import com.project.viver.ui.theme.surfaceContainerLowLightMediumContrast
import com.project.viver.ui.theme.surfaceContainerLowestDark
import com.project.viver.ui.theme.surfaceContainerLowestDarkHighContrast
import com.project.viver.ui.theme.surfaceContainerLowestDarkMediumContrast
import com.project.viver.ui.theme.surfaceContainerLowestLight
import com.project.viver.ui.theme.surfaceContainerLowestLightHighContrast
import com.project.viver.ui.theme.surfaceContainerLowestLightMediumContrast
import com.project.viver.ui.theme.surfaceDark
import com.project.viver.ui.theme.surfaceDarkHighContrast
import com.project.viver.ui.theme.surfaceDarkMediumContrast
import com.project.viver.ui.theme.surfaceDimDark
import com.project.viver.ui.theme.surfaceDimDarkHighContrast
import com.project.viver.ui.theme.surfaceDimDarkMediumContrast
import com.project.viver.ui.theme.surfaceDimLight
import com.project.viver.ui.theme.surfaceDimLightHighContrast
import com.project.viver.ui.theme.surfaceDimLightMediumContrast
import com.project.viver.ui.theme.surfaceLight
import com.project.viver.ui.theme.surfaceLightHighContrast
import com.project.viver.ui.theme.surfaceLightMediumContrast
import com.project.viver.ui.theme.surfaceVariantDark
import com.project.viver.ui.theme.surfaceVariantDarkHighContrast
import com.project.viver.ui.theme.surfaceVariantDarkMediumContrast
import com.project.viver.ui.theme.surfaceVariantLight
import com.project.viver.ui.theme.surfaceVariantLightHighContrast
import com.project.viver.ui.theme.surfaceVariantLightMediumContrast
import com.project.viver.ui.theme.tertiaryContainerDark
import com.project.viver.ui.theme.tertiaryContainerDarkHighContrast
import com.project.viver.ui.theme.tertiaryContainerDarkMediumContrast
import com.project.viver.ui.theme.tertiaryContainerLight
import com.project.viver.ui.theme.tertiaryContainerLightHighContrast
import com.project.viver.ui.theme.tertiaryContainerLightMediumContrast
import com.project.viver.ui.theme.tertiaryDark
import com.project.viver.ui.theme.tertiaryDarkHighContrast
import com.project.viver.ui.theme.tertiaryDarkMediumContrast
import com.project.viver.ui.theme.tertiaryLight
import com.project.viver.ui.theme.tertiaryLightHighContrast
import com.project.viver.ui.theme.tertiaryLightMediumContrast

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

private val mediumContrastLightColorScheme = lightColorScheme(
    primary = primaryLightMediumContrast,
    onPrimary = onPrimaryLightMediumContrast,
    primaryContainer = primaryContainerLightMediumContrast,
    onPrimaryContainer = onPrimaryContainerLightMediumContrast,
    secondary = secondaryLightMediumContrast,
    onSecondary = onSecondaryLightMediumContrast,
    secondaryContainer = secondaryContainerLightMediumContrast,
    onSecondaryContainer = onSecondaryContainerLightMediumContrast,
    tertiary = tertiaryLightMediumContrast,
    onTertiary = onTertiaryLightMediumContrast,
    tertiaryContainer = tertiaryContainerLightMediumContrast,
    onTertiaryContainer = onTertiaryContainerLightMediumContrast,
    error = errorLightMediumContrast,
    onError = onErrorLightMediumContrast,
    errorContainer = errorContainerLightMediumContrast,
    onErrorContainer = onErrorContainerLightMediumContrast,
    background = backgroundLightMediumContrast,
    onBackground = onBackgroundLightMediumContrast,
    surface = surfaceLightMediumContrast,
    onSurface = onSurfaceLightMediumContrast,
    surfaceVariant = surfaceVariantLightMediumContrast,
    onSurfaceVariant = onSurfaceVariantLightMediumContrast,
    outline = outlineLightMediumContrast,
    outlineVariant = outlineVariantLightMediumContrast,
    scrim = scrimLightMediumContrast,
    inverseSurface = inverseSurfaceLightMediumContrast,
    inverseOnSurface = inverseOnSurfaceLightMediumContrast,
    inversePrimary = inversePrimaryLightMediumContrast,
    surfaceDim = surfaceDimLightMediumContrast,
    surfaceBright = surfaceBrightLightMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestLightMediumContrast,
    surfaceContainerLow = surfaceContainerLowLightMediumContrast,
    surfaceContainer = surfaceContainerLightMediumContrast,
    surfaceContainerHigh = surfaceContainerHighLightMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestLightMediumContrast,
)

private val highContrastLightColorScheme = lightColorScheme(
    primary = primaryLightHighContrast,
    onPrimary = onPrimaryLightHighContrast,
    primaryContainer = primaryContainerLightHighContrast,
    onPrimaryContainer = onPrimaryContainerLightHighContrast,
    secondary = secondaryLightHighContrast,
    onSecondary = onSecondaryLightHighContrast,
    secondaryContainer = secondaryContainerLightHighContrast,
    onSecondaryContainer = onSecondaryContainerLightHighContrast,
    tertiary = tertiaryLightHighContrast,
    onTertiary = onTertiaryLightHighContrast,
    tertiaryContainer = tertiaryContainerLightHighContrast,
    onTertiaryContainer = onTertiaryContainerLightHighContrast,
    error = errorLightHighContrast,
    onError = onErrorLightHighContrast,
    errorContainer = errorContainerLightHighContrast,
    onErrorContainer = onErrorContainerLightHighContrast,
    background = backgroundLightHighContrast,
    onBackground = onBackgroundLightHighContrast,
    surface = surfaceLightHighContrast,
    onSurface = onSurfaceLightHighContrast,
    surfaceVariant = surfaceVariantLightHighContrast,
    onSurfaceVariant = onSurfaceVariantLightHighContrast,
    outline = outlineLightHighContrast,
    outlineVariant = outlineVariantLightHighContrast,
    scrim = scrimLightHighContrast,
    inverseSurface = inverseSurfaceLightHighContrast,
    inverseOnSurface = inverseOnSurfaceLightHighContrast,
    inversePrimary = inversePrimaryLightHighContrast,
    surfaceDim = surfaceDimLightHighContrast,
    surfaceBright = surfaceBrightLightHighContrast,
    surfaceContainerLowest = surfaceContainerLowestLightHighContrast,
    surfaceContainerLow = surfaceContainerLowLightHighContrast,
    surfaceContainer = surfaceContainerLightHighContrast,
    surfaceContainerHigh = surfaceContainerHighLightHighContrast,
    surfaceContainerHighest = surfaceContainerHighestLightHighContrast,
)

private val mediumContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkMediumContrast,
    onPrimary = onPrimaryDarkMediumContrast,
    primaryContainer = primaryContainerDarkMediumContrast,
    onPrimaryContainer = onPrimaryContainerDarkMediumContrast,
    secondary = secondaryDarkMediumContrast,
    onSecondary = onSecondaryDarkMediumContrast,
    secondaryContainer = secondaryContainerDarkMediumContrast,
    onSecondaryContainer = onSecondaryContainerDarkMediumContrast,
    tertiary = tertiaryDarkMediumContrast,
    onTertiary = onTertiaryDarkMediumContrast,
    tertiaryContainer = tertiaryContainerDarkMediumContrast,
    onTertiaryContainer = onTertiaryContainerDarkMediumContrast,
    error = errorDarkMediumContrast,
    onError = onErrorDarkMediumContrast,
    errorContainer = errorContainerDarkMediumContrast,
    onErrorContainer = onErrorContainerDarkMediumContrast,
    background = backgroundDarkMediumContrast,
    onBackground = onBackgroundDarkMediumContrast,
    surface = surfaceDarkMediumContrast,
    onSurface = onSurfaceDarkMediumContrast,
    surfaceVariant = surfaceVariantDarkMediumContrast,
    onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
    outline = outlineDarkMediumContrast,
    outlineVariant = outlineVariantDarkMediumContrast,
    scrim = scrimDarkMediumContrast,
    inverseSurface = inverseSurfaceDarkMediumContrast,
    inverseOnSurface = inverseOnSurfaceDarkMediumContrast,
    inversePrimary = inversePrimaryDarkMediumContrast,
    surfaceDim = surfaceDimDarkMediumContrast,
    surfaceBright = surfaceBrightDarkMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkMediumContrast,
    surfaceContainerLow = surfaceContainerLowDarkMediumContrast,
    surfaceContainer = surfaceContainerDarkMediumContrast,
    surfaceContainerHigh = surfaceContainerHighDarkMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkMediumContrast,
)

private val highContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkHighContrast,
    onPrimary = onPrimaryDarkHighContrast,
    primaryContainer = primaryContainerDarkHighContrast,
    onPrimaryContainer = onPrimaryContainerDarkHighContrast,
    secondary = secondaryDarkHighContrast,
    onSecondary = onSecondaryDarkHighContrast,
    secondaryContainer = secondaryContainerDarkHighContrast,
    onSecondaryContainer = onSecondaryContainerDarkHighContrast,
    tertiary = tertiaryDarkHighContrast,
    onTertiary = onTertiaryDarkHighContrast,
    tertiaryContainer = tertiaryContainerDarkHighContrast,
    onTertiaryContainer = onTertiaryContainerDarkHighContrast,
    error = errorDarkHighContrast,
    onError = onErrorDarkHighContrast,
    errorContainer = errorContainerDarkHighContrast,
    onErrorContainer = onErrorContainerDarkHighContrast,
    background = backgroundDarkHighContrast,
    onBackground = onBackgroundDarkHighContrast,
    surface = surfaceDarkHighContrast,
    onSurface = onSurfaceDarkHighContrast,
    surfaceVariant = surfaceVariantDarkHighContrast,
    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
    outline = outlineDarkHighContrast,
    outlineVariant = outlineVariantDarkHighContrast,
    scrim = scrimDarkHighContrast,
    inverseSurface = inverseSurfaceDarkHighContrast,
    inverseOnSurface = inverseOnSurfaceDarkHighContrast,
    inversePrimary = inversePrimaryDarkHighContrast,
    surfaceDim = surfaceDimDarkHighContrast,
    surfaceBright = surfaceBrightDarkHighContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
    surfaceContainerLow = surfaceContainerLowDarkHighContrast,
    surfaceContainer = surfaceContainerDarkHighContrast,
    surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
)

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

@Composable
fun ViverTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable() () -> Unit
) {
  val colorScheme = when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
          val context = LocalContext.current
          if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      
      darkTheme -> darkScheme
      else -> lightScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,

    typography = AppTypography,
    content = content
  )
}

