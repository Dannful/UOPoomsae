package com.github.dannful.uopoomsae.presentation.standard.standard_technique

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.dannful.uopoomsae.core.Route
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
        List(
            savedStateHandle.get<Int>(Route.StandardTechnique::count.name) ?: 1
        ) {
            INITIAL_SCORE
        })

    fun setScore(index: Int, score: Float) {
        val currentList = techniqueScore.value.toMutableList()
        currentList[index] = score.coerceIn(0f..4f)
        savedStateHandle[TECHNIQUE_SCORE_KEY] = currentList
    }
}