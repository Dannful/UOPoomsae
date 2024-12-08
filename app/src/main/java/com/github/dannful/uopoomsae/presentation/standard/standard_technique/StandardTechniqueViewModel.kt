package com.github.dannful.uopoomsae.presentation.standard.standard_technique

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class StandardTechniqueViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {

        private const val TECHNIQUE_SCORE_KEY = "techniqueScore"
        private const val INITIAL_SCORE = 4.0f
    }

    val techniqueScore = savedStateHandle.getStateFlow(
        TECHNIQUE_SCORE_KEY,
        INITIAL_SCORE
    )

    fun setScore(score: Float) {
        savedStateHandle[TECHNIQUE_SCORE_KEY] = score.coerceIn(0f..4f)
    }
}