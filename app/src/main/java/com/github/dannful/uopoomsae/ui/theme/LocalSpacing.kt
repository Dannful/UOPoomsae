package com.github.dannful.uopoomsae.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Spacing(
    val tiny: Dp,
    val small: Dp,
    val medium: Dp,
    val large: Dp,
    val huge: Dp
)

val LocalSpacing = androidx.compose.runtime.staticCompositionLocalOf<Spacing> {
    error("No Spacing provided")
}

val DefaultSpacing = Spacing(
    tiny = 4.dp,
    small = 8.dp,
    medium = 16.dp,
    large = 24.dp,
    huge = 32.dp
)