package com.github.dannful.uopoomsae.presentation.standard.standard_presentation

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dannful.uopoomsae.core.Route
import com.github.dannful.uopoomsae.domain.model.Permissions
import com.github.dannful.uopoomsae.domain.model.ScoreData
import com.github.dannful.uopoomsae.domain.repository.PreferencesRepository
import com.github.dannful.uopoomsae.domain.repository.RemoteRepository
import com.github.dannful.uopoomsae.presentation.concurrent.concurrent_presentation.PresentationScore
import com.github.dannful.uopoomsae.presentation.core.displayRequestFailure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StandardPresentationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val remoteRepository: RemoteRepository,
    private val preferencesRepository: PreferencesRepository,
    private val application: Application
) : ViewModel() {

    companion object {

        private const val SCORES_KEY = "presentation_scores"

        private const val INITIAL_SCORE = 0.5f

        val values = listOf(
            0.5f,
            0.6f,
            0.7f,
            0.8f,
            0.9f,
            1.0f,
            1.1f,
            1.2f,
            1.3f,
            1.4f,
            1.5f,
            1.6f,
            1.7f,
            1.8f,
            1.9f,
            2.0f
        )
    }

    val previousScore =
        savedStateHandle.get<Float>(Route.StandardPresentation::techniqueScore.name)!!
    val scores = savedStateHandle.getStateFlow(
        SCORES_KEY,
        floatArrayOf(INITIAL_SCORE, INITIAL_SCORE, INITIAL_SCORE)
    )

    fun setValue(score: FloatArray) {
        savedStateHandle[SCORES_KEY] = score
    }

    fun setSpeed(value: Float) {
        setValue(floatArrayOf(value, scores.value[1], scores.value[2]))
    }

    fun setPace(value: Float) {
        setValue(floatArrayOf(scores.value[0], value, scores.value[2]))
    }

    fun setPower(value: Float) {
        setValue(floatArrayOf(scores.value[0], scores.value[1], value))
    }

    fun calculateScore(): Float {
        val score = scores.value
        return score[0] + score[1] + score[2]
    }

    fun send() {
        viewModelScope.launch {
            val authUser = preferencesRepository.getCurrentAuth().firstOrNull() ?: return@launch
            if (authUser.level < Permissions.USER.level) return@launch
            val competitionMode = preferencesRepository.getCompetitionMode().firstOrNull() ?: false
            if (!competitionMode) return@launch
            val tableId = preferencesRepository.getTableId().firstOrNull() ?: run {
                displayRequestFailure(application)
                return@launch
            }
            val judgeId = preferencesRepository.getJudgeId().firstOrNull() ?: run {
                displayRequestFailure(application)
                return@launch
            }
            val result = remoteRepository.sendScore(
                ScoreData(
                    tableId = tableId.toShort(),
                    judgeId = judgeId.toShort(),
                    accuracyScores = listOf(previousScore),
                    presentationScores = listOf(calculateScore())
                )
            )
            if (result.isFailure)
                displayRequestFailure(application)
        }
    }
}