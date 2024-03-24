package com.github.dannful.uopoomsae.presentation.scores_receiver

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dannful.uopoomsae.domain.repository.PreferencesRepository
import com.github.dannful.uopoomsae.domain.repository.ScoreRepository
import com.github.dannful.uopoomsae.presentation.core.ScoreBundle
import com.github.dannful.uopoomsae.presentation.core.displayRequestFailure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoresReceiverViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val scoreRepository: ScoreRepository,
    private val preferencesRepository: PreferencesRepository,
    private val application: Application
) : ViewModel() {

    companion object {

        private const val SCORES_KEY = "scores"
        private const val FETCH_COOLDOWN_KEY = "last"
        private const val FETCH_COOLDOWN_SECONDS = 5
        const val JUDGE_COUNT = 5
    }

    val scores =
        savedStateHandle.getStateFlow(SCORES_KEY, List(JUDGE_COUNT) { ScoreBundle(0f, 0f) })

    val lastFetch = savedStateHandle.getStateFlow(FETCH_COOLDOWN_KEY, 0)

    init {
        fetchScores()
    }

    private fun initFetchCountdown() {
        viewModelScope.launch {
            while (lastFetch.value > 0) {
                savedStateHandle[FETCH_COOLDOWN_KEY] = lastFetch.value - 1
                delay(1000)
            }
        }
    }

    fun fetchScores() {
        viewModelScope.launch {
            if (lastFetch.value > 0) return@launch
            val competitionMode = preferencesRepository.getCompetitionMode().firstOrNull() ?: true
            val tableId = preferencesRepository.getTableId().firstOrNull() ?: run {
                if (competitionMode) displayRequestFailure(application)
                return@launch
            }
            val result = scoreRepository.getScores(tableId)
            if (result.isFailure && competitionMode) {
                displayRequestFailure(application)
                return@launch
            }
            result.getOrThrow().forEach { scoreData ->
                setScore(
                    scoreData.judgeId - 1,
                    ScoreBundle(
                        presentationScore = scoreData.presentationScore,
                        techniqueScore = scoreData.accuracyScore
                    )
                )
            }
            savedStateHandle[FETCH_COOLDOWN_KEY] = FETCH_COOLDOWN_SECONDS
            initFetchCountdown()
        }
    }

    private fun setScore(index: Int, score: ScoreBundle) {
        val newScores = scores.value.toMutableList()
        newScores[index] = score
        savedStateHandle[SCORES_KEY] = newScores
    }

    fun resetScores() {
        savedStateHandle[SCORES_KEY] = List(JUDGE_COUNT) { ScoreBundle(0f, 0f) }
    }
}