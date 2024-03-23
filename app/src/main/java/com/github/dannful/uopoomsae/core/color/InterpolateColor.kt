package com.github.dannful.uopoomsae.core.color

fun interpolateColor(startColor: Float, endColor: Float, currentIndex: Int, total: Int): Float {
    return startColor + (endColor - startColor) * currentIndex / total
}