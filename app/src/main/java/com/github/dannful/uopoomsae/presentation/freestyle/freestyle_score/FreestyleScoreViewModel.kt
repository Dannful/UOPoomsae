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
        const val STANCES_KEY = "stances"
    }

    val scores = savedStateHandle.getStateFlow(
        SCORES_KEY,
        List(10) {
            1f
        }.toFloatArray()
    )

    val stances = savedStateHandle.getStateFlow(
        STANCES_KEY,
        booleanArrayOf(false, false, false)
    )

    fun setScoreIndex(scoreIndex: Int, score: Float) {
        val newScores = scores.value.clone()
        newScores[scoreIndex] = score
        savedStateHandle[SCORES_KEY] = newScores
    }

    fun setFirstStance(stance: Boolean) {
        val newStances = stances.value.clone()
        newStances[0] = stance
        savedStateHandle[STANCES_KEY] = newStances
    }

    fun setSecondStance(stance: Boolean) {
        val newStances = stances.value.clone()
        newStances[1] = stance
        savedStateHandle[STANCES_KEY] = newStances
    }

    fun setThirdStance(stance: Boolean) {
        val newStances = stances.value.clone()
        newStances[2] = stance
        savedStateHandle[STANCES_KEY] = newStances
    }

    fun calculateTechnique(): Float {
        // technique score is given by first give values
        return scores.value.take(5).sum()
    }

    fun calculatePresentation(): Float {
        return scores.value.sum() - calculateTechnique()
    }

    fun calculateStanceDecrease(): Float {
        val stance = stances.value
        var decrease = 0f
        if (!stance[0])
            decrease += 0.3f
        if (!stance[1])
            decrease += 0.3f
        if (!stance[2])
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
            val result = remoteRepository.sendScore(
                ScoreData(
                    judgeId = judgeId.toShort(),
                    tableId = tableId.toShort(),
                    presentationScores = listOf(calculatePresentation()),
                    accuracyScores = listOf(calculateTechnique())
                )
            )
            if (result.isFailure) displayRequestFailure(application)
        }
    }
}