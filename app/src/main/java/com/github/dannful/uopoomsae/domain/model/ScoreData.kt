package com.github.dannful.uopoomsae.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ScoreData(
    val tableId: Int,
    val judgeId: Int,
    val presentationScore: Float,
    val accuracyScore: Float
)
