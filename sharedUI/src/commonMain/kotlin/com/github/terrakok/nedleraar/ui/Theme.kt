package com.github.terrakok.nedleraar.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.materialkolor.DynamicMaterialTheme
import com.materialkolor.PaletteStyle
import com.materialkolor.rememberDynamicMaterialThemeState

object Colors {
    val Primary = Color(0xFF8FA592)
    val Secondary = Color(0xFF6E9075)
    val Tertiary = Color(0xFFD0DAD2)
    val Error = Color(0xFFD99090)
    val Neutral = Color(0xFFFDFFFE)
    val NeutralVariant = Color(0xFFFFFFFF)
}

internal val LocalThemeIsDark = compositionLocalOf { mutableStateOf(true) }

@Composable
internal fun AppTheme(
    onThemeChanged: @Composable (isDark: Boolean) -> Unit = {},
    content: @Composable () -> Unit
) {
    val systemIsDark = isSystemInDarkTheme()
    val isDarkState = remember(systemIsDark) { mutableStateOf(systemIsDark) }
    CompositionLocalProvider(
        LocalThemeIsDark provides isDarkState
    ) {
        val isDark by isDarkState
        onThemeChanged(!isDark)

        val dynamicThemeState = rememberDynamicMaterialThemeState(
            isDark = isDark,
            style = PaletteStyle.TonalSpot,
            primary = Colors.Primary,
            secondary = Colors.Secondary,
            tertiary = Colors.Tertiary,
            error = Colors.Error,
            neutral = Colors.Neutral,
            neutralVariant = Colors.NeutralVariant,
        )

        DynamicMaterialTheme(
            state = dynamicThemeState,
            animate = true,
            content =  { Surface(content = content) },
        )
    }
}
