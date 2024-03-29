package com.github.dannful.uopoomsae.presentation.freestyle.freestyle_score

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dannful.uopoomsae.domain.model.Permissions
import com.github.dannful.uopoomsae.domain.model.ScoreData
import com.github.dannful.uopoomsae.domain.repository.PreferencesRepository
import com.github.dannful.uopoomsae.domain.repository.RemoteRepository
import com.github.dannful.uopoomsae.presentation.core.displayRequestFailure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FreestyleScoreViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val remoteRepository: RemoteRepository,
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

    val scores = savedStateHandle.getStateFlow(SCORES_KEY, List(10) { values.lastIndex })
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
        return scores.value.map { values[it] }.sum() - calculateTechnique()
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

    fun send() {
        viewModelScope.launch {
            val authUser = preferencesRepository.getCurrentAuth().firstOrNull() ?: return@launch
            if (authUser.level < Permissions.USER.level) return@launch
            val competitionMode = preferencesRepository.getCompetitionMode().firstOrNull() ?: false
            if (!competitionMode) return@launch
            val judgeId = preferencesRepository.getJudgeId().firstOrNull() ?: run {
                displayRequestFailure(application)
                return@launch
            }
            val tableId = preferencesRepository.getTableId().firstOrNull() ?: run {
                displayRequestFailure(application)
                return@launch
            }
            val presentationScore = calculatePresentation()
            val accuracyScore = calculateTechnique()
            val result = remoteRepository.sendScore(
                ScoreData(
                    judgeId = judgeId.toShort(),
                    tableId = tableId.toShort(),
                    presentationScore = presentationScore,
                    accuracyScore = accuracyScore
                )
            )
            if (result.isFailure) displayRequestFailure(application)
        }
    }
}