package com.github.dannful.uopoomsae.presentation.freestyle.freestyle_score

data class FreestyleScore(
    val score: List<Float>,
    val firstStance: Boolean,
    val secondStance: Boolean,
    val thirdStance: Boolean
)
