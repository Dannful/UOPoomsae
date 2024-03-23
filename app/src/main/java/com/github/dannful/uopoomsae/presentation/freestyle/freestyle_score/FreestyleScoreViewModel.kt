package com.github.dannful.uopoomsae.presentation.freestyle.freestyle_score

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dannful.uopoomsae.domain.model.ScoreData
import com.github.dannful.uopoomsae.domain.repository.PreferencesRepository
import com.github.dannful.uopoomsae.domain.repository.ScoreRepository
import com.github.dannful.uopoomsae.presentation.core.displayRequestFailure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FreestyleScoreViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val scoreRepository: ScoreRepository,
    private val preferencesRepository: PreferencesRepository,
    private val application: Application
) : ViewModel() {

    companion object {

        val values = listOf(
            0f, 0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1f
        )

        const val SCORES_KEY = "scores"
        const val FIRST_STANCE_KEY = "firstStance"
        const val SECOND_STANCE_KEY = "secondStance"
        const val THIRD_STANCE_KEY = "thirdStance"
    }

    val scores = savedStateHandle.getStateFlow(SCORES_KEY, List(values.size) { values.lastIndex })
    val firstStance = savedStateHandle.getStateFlow(FIRST_STANCE_KEY, false)
    val secondStance = savedStateHandle.getStateFlow(SECOND_STANCE_KEY, false)
    val thirdStance = savedStateHandle.getStateFlow(THIRD_STANCE_KEY, false)

    fun setScoreIndex(index: Int, scoreIndex: Int) {
        val newScores = scores.value.toMutableList()
        newScores[index] = scoreIndex
        savedStateHandle[SCORES_KEY] = newScores
    }

    fun setFirstStance(stance: Boolean) {
        savedStateHandle[FIRST_STANCE_KEY] = stance
    }

    fun setSecondStance(stance: Boolean) {
        savedStateHandle[SECOND_STANCE_KEY] = stance
    }

    fun setThirdStance(stance: Boolean) {
        savedStateHandle[THIRD_STANCE_KEY] = stance
    }

    fun calculateTechnique(): Float {
        // technique score is given by first give values
        return scores.value.take(5).map { values[it] }.sum()
    }

    fun calculatePresentation(): Float {
        return (scores.value - (scores.value.take(5)).toSet()).map { values[it] }.sum()
    }

    fun calculateStanceDecrease(): Float {
        var decrease = 0f
        if (!firstStance.value)
            decrease += 0.3f
        if (!secondStance.value)
            decrease += 0.3f
        if (!thirdStance.value)
            decrease += 0.3f
        return decrease
    }

    fun calculateScore(): Float {
        val totalScore = scores.value.map { values[it] }.sum()
        return totalScore - calculateStanceDecrease()
    }

    fun send() {
        viewModelScope.launch {
            val competitionMode = preferencesRepository.getCompetitionMode().firstOrNull() ?: true
            val judgeId = preferencesRepository.getJudgeId().firstOrNull() ?: run {
                if (competitionMode) displayRequestFailure(application)
                return@launch
            }
            val tableId = preferencesRepository.getTableId().firstOrNull() ?: run {
                if (competitionMode) displayRequestFailure(application)
                return@launch
            }
            val result = scoreRepository.sendScore(
                ScoreData(
                    judgeId = judgeId,
                    tableId = tableId,
                    presentationScore = calculatePresentation(),
                    accuracyScore = calculateTechnique()
                )
            )
            if (result.isFailure && competitionMode) displayRequestFailure(application)
        }
    }
}