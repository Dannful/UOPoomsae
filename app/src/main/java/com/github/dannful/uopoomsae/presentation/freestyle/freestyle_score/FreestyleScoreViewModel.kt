package com.github.dannful.uopoomsae.presentation.freestyle.freestyle_score

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dannful.uopoomsae.core.Route
import com.github.dannful.uopoomsae.domain.model.Permissions
import com.github.dannful.uopoomsae.domain.model.ScoreData
import com.github.dannful.uopoomsae.domain.repository.PreferencesRepository
import com.github.dannful.uopoomsae.domain.repository.RemoteRepository
import com.github.dannful.uopoomsae.presentation.core.displayRequestFailure
import com.github.dannful.uopoomsae.presentation.standard.standard_presentation.PresentationScore
import com.github.dannful.uopoomsae.presentation.standard.standard_presentation.StandardPresentationViewModel
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
    }

    val scores = savedStateHandle.getStateFlow(
        SCORES_KEY,
        List(
            savedStateHandle.get<Int>(
                Route.FreestyleScore::count.name
            ) ?: 1
        ) {
            FreestyleScore(
                scores = List(10) {
                    1f
                },
                firstStance = false,
                secondStance = false,
                thirdStance = false
            )
        })

    fun setValue(index: Int, score: FreestyleScore) {
        val map = scores.value.toMutableList()
        map[index] = score
        savedStateHandle[SCORES_KEY] = map
    }

    fun setScoreIndex(index: Int, scoreIndex: Int, score: Float) {
        val newScores = scores.value[index]
        val list = newScores.scores.toMutableList()
        list[scoreIndex] = score
        savedStateHandle[SCORES_KEY] = newScores.copy(
            scores = list
        )
    }

    fun setFirstStance(index: Int, stance: Boolean) {
        val newScores = scores.value[index]
        savedStateHandle[SCORES_KEY] = newScores.copy(
            firstStance = stance
        )
    }

    fun setSecondStance(index: Int, stance: Boolean) {
        val newScores = scores.value[index]
        savedStateHandle[SCORES_KEY] = newScores.copy(
            secondStance = stance
        )
    }

    fun setThirdStance(index: Int, stance: Boolean) {
        val newScores = scores.value[index]
        savedStateHandle[SCORES_KEY] = newScores.copy(
            thirdStance = stance
        )
    }

    fun calculateTechnique(index: Int): Float {
        // technique score is given by first give values
        return scores.value[index].scores.take(5).sum()
    }

    fun calculatePresentation(index: Int): Float {
        return scores.value[index].scores.sum() - calculateTechnique(index)
    }

    fun calculateStanceDecrease(index: Int): Float {
        val score = scores.value[index]
        var decrease = 0f
        if (!score.firstStance)
            decrease += 0.3f
        if (!score.secondStance)
            decrease += 0.3f
        if (!score.thirdStance)
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
                    presentationScores = List(scores.value.size) { index ->
                        calculatePresentation(
                            index
                        )
                    },
                    accuracyScores = List(scores.value.size) { index -> calculateTechnique(index) }
                )
            )
            if (result.isFailure) displayRequestFailure(application)
        }
    }
}