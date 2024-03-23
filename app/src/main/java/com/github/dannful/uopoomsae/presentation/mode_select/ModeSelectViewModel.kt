package com.github.dannful.uopoomsae.presentation.mode_select

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dannful.uopoomsae.domain.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModeSelectViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {

        private const val JUDGE_KEY = "JUDGE"
        private const val TABLE_KEY = "TABLE"
    }

    val judge = savedStateHandle.getStateFlow(JUDGE_KEY, "1")
    val table = savedStateHandle.getStateFlow(TABLE_KEY, "1")
    val competitionMode = preferencesRepository.getCompetitionMode()

    init {
        preferencesRepository.getJudgeId().onEach {
            setJudgeId(it.toString())
        }.launchIn(viewModelScope)
        preferencesRepository.getTableId().onEach {
            setTableId(it.toString())
        }.launchIn(viewModelScope)
    }

    fun updateCompetitionMode(competitionMode: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setCompetitionMode(competitionMode)
        }
    }

    fun setJudgeId(judgeId: String) {
        savedStateHandle[JUDGE_KEY] = judgeId
    }

    fun setTableId(tableId: String) {
        savedStateHandle[TABLE_KEY] = tableId
    }

    fun submit() {
        viewModelScope.launch {
            val convertedJudge = judge.value.toIntOrNull() ?: return@launch
            val convertedTable = table.value.toIntOrNull() ?: return@launch
            preferencesRepository.saveJudgeId(convertedJudge)
            preferencesRepository.saveTableId(convertedTable)
        }
    }
}