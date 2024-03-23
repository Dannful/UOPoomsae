package com.github.dannful.uopoomsae.core

data class RouteArgument(
    val name: String,
    val optional: Boolean = false
) {

    override fun toString(): String {
        return name
    }
}
