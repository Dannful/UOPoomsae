package com.github.dannful.uopoomsae.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ScoreData(
    val tableId: Short,
    val judgeId: Short,
    val presentationScore: Float,
    val accuracyScore: Float
)
