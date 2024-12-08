package com.github.dannful.uopoomsae.presentation.concurrent.concurrent_technique

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.dannful.uopoomsae.core.Route
import com.github.dannful.uopoomsae.presentation.concurrent.core.ConcurrentScore
import javax.inject.Inject

class ConcurrentTechniqueViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {

        private const val TECHNIQUE_SCORE_KEY = "techniqueScore"
        private const val INITIAL_SCORE = 4.0f
    }

    val techniqueScore = savedStateHandle.getStateFlow(
        TECHNIQUE_SCORE_KEY,
        floatArrayOf(INITIAL_SCORE, INITIAL_SCORE)
    )

    fun setScore(firstScore: Float? = null, secondScore: Float? = null) {
        savedStateHandle[TECHNIQUE_SCORE_KEY] = floatArrayOf(
            firstScore?.coerceIn(0f..4f) ?: techniqueScore.value[0],
            secondScore?.coerceIn(0f..4f) ?: techniqueScore.value[1]
        )
    }
}