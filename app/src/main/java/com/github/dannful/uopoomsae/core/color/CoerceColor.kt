package com.github.dannful.uopoomsae.core.color

fun coerceColor(color: Float): Float {
    return color.coerceIn(0f..255f)
}