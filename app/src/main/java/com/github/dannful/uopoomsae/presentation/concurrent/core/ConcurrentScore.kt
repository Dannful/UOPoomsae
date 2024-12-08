package com.github.dannful.uopoomsae.presentation.concurrent.core

import kotlinx.serialization.Serializable

@Serializable
data class ConcurrentScore(
    val firstScore: Float,
    val secondScore: Float
)
