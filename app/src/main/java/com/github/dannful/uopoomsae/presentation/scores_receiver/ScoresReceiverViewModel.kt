package com.github.dannful.uopoomsae.presentation.scores_receiver

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dannful.uopoomsae.domain.repository.DispatcherProvider
import com.github.dannful.uopoomsae.domain.repository.PreferencesRepository
import com.github.dannful.uopoomsae.domain.repository.RemoteRepository
import com.github.dannful.uopoomsae.presentation.core.ScoreBundle
import com.github.dannful.uopoomsae.presentation.core.displayRequestFailure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ScoresReceiverViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val remoteRepository: RemoteRepository,
    private val preferencesRepository: PreferencesRepository,
    private val application: Application,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    companion object {

        private const val SCORES_KEY = "scores"
        private const val FETCH_COOLDOWN_KEY = "last"
        private const val FETCH_COOLDOWN_SECONDS = 5
        const val JUDGE_COUNT = 5
    }

    val scores =
        savedStateHandle.getStateFlow(
            SCORES_KEY,
            listOf(List(JUDGE_COUNT) { ScoreBundle(0f, 0f) })
        )

    val lastFetch = savedStateHandle.getStateFlow(FETCH_COOLDOWN_KEY, 0)
    val isCompetitionMode = preferencesRepository.getCompetitionMode()
    val changes = mutableStateListOf<Int>()

    init {
        resetScores()
        fetchScores()
    }

    private fun initFetchCountdown() {
        viewModelScope.launch(dispatcherProvider.IO) {
            while (lastFetch.value > 0) {
                withContext(dispatcherProvider.Main) {
                    savedStateHandle[FETCH_COOLDOWN_KEY] = lastFetch.value - 1
                }
                delay(1000)
            }
        }
    }

    private fun addRecentUpdate(judgeId: Int) {
        changes.add(judgeId)
    }

    fun fetchScores() {
        viewModelScope.launch {
            if (lastFetch.value > 0) return@launch
            val competitionMode =
                isCompetitionMode.firstOrNull() ?: true
            if (!competitionMode) return@launch
            val tableId = preferencesRepository.getTableId().firstOrNull() ?: run {
                displayRequestFailure(application)
                return@launch
            }
            val result = remoteRepository.getScores(tableId.toShort())
            if (result.isFailure) {
                displayRequestFailure(application)
                return@launch
            }
            result.getOrThrow().forEach { scoreData ->
                setScore(
                    scoreData.judgeId - 1,
                    scoreData.accuracyScores.mapIndexed { index, value ->
                        ScoreBundle(
                            techniqueScore = value,
                            presentationScore = scoreData.presentationScores[index]
                        )
                    }
                )
            }
            savedStateHandle[FETCH_COOLDOWN_KEY] = FETCH_COOLDOWN_SECONDS
            initFetchCountdown()
        }
    }

    private fun setScore(judgeIndex: Int, score: List<ScoreBundle>) {
        val newScores = scores.value.toMutableList()
        newScores[judgeIndex] = score
        savedStateHandle[SCORES_KEY] = newScores
        addRecentUpdate(judgeIndex)
    }

    fun resetScores() {
        savedStateHandle[SCORES_KEY] = listOf(List(JUDGE_COUNT) { ScoreBundle(0f, 0f) })
        changes.clear()
    }
}