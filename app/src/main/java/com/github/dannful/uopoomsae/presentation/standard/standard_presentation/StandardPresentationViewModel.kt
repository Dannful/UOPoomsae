package com.github.dannful.uopoomsae.presentation.standard.standard_presentation

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dannful.uopoomsae.core.Route
import com.github.dannful.uopoomsae.domain.model.ScoreData
import com.github.dannful.uopoomsae.domain.repository.PreferencesRepository
import com.github.dannful.uopoomsae.domain.repository.ScoreRepository
import com.github.dannful.uopoomsae.presentation.core.displayRequestFailure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StandardPresentationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val scoreRepository: ScoreRepository,
    private val preferencesRepository: PreferencesRepository,
    private val application: Application
) : ViewModel() {

    companion object {

        private const val SPEED_KEY = "speed"
        private const val PACE_KEY = "pace"
        private const val POWER_KEY = "power"

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

    val speed = savedStateHandle.getStateFlow(SPEED_KEY, values.lastIndex)
    val pace = savedStateHandle.getStateFlow(PACE_KEY, values.lastIndex)
    val power = savedStateHandle.getStateFlow(POWER_KEY, values.lastIndex)
    val previousScore =
        savedStateHandle.get<Float>(Route.StandardPresentation.arguments[0].toString())

    fun setSpeed(index: Int) {
        savedStateHandle[SPEED_KEY] = index
    }

    fun setPace(index: Int) {
        savedStateHandle[PACE_KEY] = index
    }

    fun setPower(index: Int) {
        savedStateHandle[POWER_KEY] = index
    }

    fun calculateScore(): Float {
        return values[speed.value] + values[pace.value] + values[power.value]
    }

    fun send() {
        viewModelScope.launch {
            val competitionMode = preferencesRepository.getCompetitionMode().firstOrNull() ?: true
            val tableId = preferencesRepository.getTableId().firstOrNull() ?: run {
                if (competitionMode) displayRequestFailure(application)
                return@launch
            }
            val judgeId = preferencesRepository.getJudgeId().firstOrNull() ?: run {
                if (competitionMode) displayRequestFailure(application)
                return@launch
            }
            val result = scoreRepository.sendScore(
                ScoreData(
                    tableId = tableId,
                    judgeId = judgeId,
                    accuracyScore = previousScore ?: return@launch,
                    presentationScore = calculateScore()
                )
            )
            if (result.isFailure && competitionMode)
                displayRequestFailure(application)
        }
    }
}