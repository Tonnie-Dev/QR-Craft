package com.tonyxlab.qrcraft.presentation.theme.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// One fixed color scheme for the whole app
private val AppColorScheme = lightColorScheme(
        // Brand
        primary = Primary,
        onPrimary = OnSurface,                // dark text on the yellow brand
        primaryContainer = Primary,           // single-scheme: keep it simple
        onPrimaryContainer = OnSurface,

        // We don’t have explicit secondary/tertiary palettes in Figma,
        // so we use neutrals + accent to keep hierarchy consistent.
        secondary = OnSurfaceAlt,             // subtle neutral accent
        onSecondary = SurfaceHigher,
        secondaryContainer = Surface,         // neutral container background
        onSecondaryContainer = OnSurface,

        tertiary = TextAccent,                // purple accent from Figma
        onTertiary = OnOverlay,               // white on purple
        tertiaryContainer = TextAccentBg,     // 10% chip-like background
        onTertiaryContainer = TextAccent,

        // Surfaces / background
        background = Surface,                 // app background (light grey)
        onBackground = OnSurface,
        surface = Surface,              // Edited from SurfaceHigher
        onSurface = OnSurface,
        surfaceVariant = Surface,             // subtle alt-surface
        onSurfaceVariant = OnSurfaceAlt,
        surfaceContainerHigh = SurfaceHigher, // Edited - added SurfaceHighter

        // Status
        error = Error,
        onError = OnOverlay,                  // white on error
        errorContainer = Error,               // single-scheme: same as error
        onErrorContainer = OnOverlay,

        // Misc
        outline = OnSurfaceAlt,
        outlineVariant = OnSurfaceAlt,
        scrim = Overlay,
        inverseSurface = OnSurface,           // dark surface for “inverse” cases
        inverseOnSurface = SurfaceHigher,
        inversePrimary = Primary
)

@Composable
fun QRCraftTheme(content: @Composable () -> Unit) {
    MaterialTheme(
            colorScheme = AppColorScheme,
            typography = Typography,
            content = content,
            shapes = customMaterialShapes
    )
}