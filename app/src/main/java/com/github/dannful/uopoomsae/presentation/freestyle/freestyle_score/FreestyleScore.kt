package com.github.dannful.uopoomsae.presentation.freestyle.freestyle_score

data class FreestyleScore(
    val scores: List<Float>,
    val firstStance: Boolean,
    val secondStance: Boolean,
    val thirdStance: Boolean
)
