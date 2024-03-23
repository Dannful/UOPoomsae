package com.github.dannful.uopoomsae.core

import java.text.DecimalFormat

fun formatDecimal(value: Float): String {
    return DecimalFormat("#.0").format(value)
}