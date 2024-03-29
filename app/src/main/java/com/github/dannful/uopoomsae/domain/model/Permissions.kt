package com.github.dannful.uopoomsae.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class Permissions(
    val level: Short
) {

    NONE(0),
    USER(1),
    ADMIN(2)
}