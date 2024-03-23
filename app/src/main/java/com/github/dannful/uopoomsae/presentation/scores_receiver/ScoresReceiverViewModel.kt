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
        const val JUDGE_COUNT = 5
    }

    val scores =
        savedStateHandle.getStateFlow(SCORES_KEY, List(JUDGE_COUNT) { ScoreBundle(0f, 0f) })

    init {
        viewModelScope.launch {
            val competitionMode = preferencesRepository.getCompetitionMode().firstOrNull() ?: true
            val tableId = preferencesRepository.getTableId().firstOrNull() ?: run {
                if (competitionMode) displayRequestFailure(application)
                return@launch
            }
            scoreRepository.scoreChannelById(tableId).catch {
                if (competitionMode) displayRequestFailure(application)
            }.collectLatest {
                setScore(
                    it.judgeId - 1,
                    ScoreBundle(
                        presentationScore = it.presentationScore,
                        techniqueScore = it.accuracyScore
                    )
                )
            }
        }
    }

    fun setScore(index: Int, score: ScoreBundle) {
        val newScores = scores.value.toMutableList()
        newScores[index] = score
        savedStateHandle[SCORES_KEY] = newScores
    }

    fun resetScores() {
        savedStateHandle[SCORES_KEY] = List(JUDGE_COUNT) { ScoreBundle(0f, 0f) }
    }
}